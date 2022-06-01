package com.karatitza.converters.inkscape.console;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import static com.karatitza.converters.inkscape.console.InkscapeShell.FILE_CREATION_WATCH_TIMEOUT_SECONDS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

class FileAcceptListener {

    private final File targetFile;
    private final WatchService watchService;

    public FileAcceptListener(File targetFile) {
        targetFile.getParentFile().mkdirs();
        this.targetFile = targetFile;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            targetFile.toPath().getParent().register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException("Failed to watch file: " + targetFile, e);

        }
    }

    public boolean accept() {
        try {
            return tryAccept();
        } catch (InterruptedException e) {
            throw new RuntimeException("File watch canceled: " + targetFile, e);
        }
    }

    private boolean tryAccept() throws InterruptedException {
        WatchKey watchedKey;
        boolean acceptFileCreated = false;
        boolean acceptFileModified = false;
        while ((watchedKey = watchService.poll(FILE_CREATION_WATCH_TIMEOUT_SECONDS, TimeUnit.SECONDS)) != null) {
            for (WatchEvent<?> watchEvent : watchedKey.pollEvents()) {
                WatchEvent.Kind<?> actualEventKind = watchEvent.kind();
                String eventContext = String.valueOf(watchEvent.context());
                InkscapeShell.LOG.info("Accept file Event kind: {}", actualEventKind);
                InkscapeShell.LOG.info("Accept file Event context: {}", eventContext);
                if (eventContext.equals(targetFile.getName()) && ENTRY_CREATE.equals(actualEventKind)) {
                    acceptFileCreated = true;
                    if (watchEvent.count() > 1) InkscapeShell.LOG.warn("Multiple file creation {}", targetFile);
                }
                if (eventContext.equals(targetFile.getName()) && ENTRY_MODIFY.equals(actualEventKind)) {
                    acceptFileModified = true;
                    if (watchEvent.count() > 1) InkscapeShell.LOG.warn("Multiple file modify {}", targetFile);
                }
                if (acceptFileCreated && acceptFileModified) {
                    return true;
                }
            }
            watchedKey.reset();
        }
        return false;
    }
}
