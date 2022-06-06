package com.karatitza.converters.inkscape.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class InkscapeShell implements AutoCloseable {
    public static final Logger LOG = LoggerFactory.getLogger(InkscapeShell.class);
    public static final int FILE_CREATION_WATCH_TIMEOUT_SECONDS = 15;

    private final Process shellProcess;
    private final PrintWriter shellOutput;
    private final File logsDir;

    public InkscapeShell() {
        this(null);
    }

    public InkscapeShell(File logsDir) {
        this.logsDir = logsDir;
        this.shellProcess = enterToInkscapeShell();
        LOG.info("Enter to Inkscape shell accepted");
        this.shellOutput = new PrintWriter(shellProcess.getOutputStream(), true) {
            @Override
            public void close() {
                LOG.info("Shell output closed");
                super.close();
            }
        };
    }

    public void exportToPdfFile(File sourceFile, File targetFile) {
        if (targetFile.exists() && !targetFile.delete()) {
            LOG.error("Failed to delete old file: {}, conversion skipped!", targetFile);
            return;
        }
        FileAcceptListener listener = new FileAcceptListener(targetFile);
        shellOutput.println("file-open:" + getCanonicalPath(sourceFile));
        shellOutput.println("export-filename:" + getCanonicalPath(targetFile));
        shellOutput.println("export-pdf-version:1.5");
        shellOutput.println("export-type:pdf");
        shellOutput.println("export-do");
        LOG.info("Export actions was enter for file {}", targetFile);
        if (!listener.accept()) {
            LOG.error("Accept Timeout exceeded for file: {}", targetFile);
        }
        shellOutput.println("file-close");
    }

    public void exportToPngFile(File sourceFile, File targetFile) {
        if (targetFile.exists() && !targetFile.delete()) {
            LOG.error("Failed to delete old file: {}, conversion skipped!", targetFile);
            return;
        }
        FileAcceptListener listener = new FileAcceptListener(targetFile);
        shellOutput.println("file-open:" + getCanonicalPath(sourceFile));
        shellOutput.println("export-filename:" + getCanonicalPath(targetFile));
        shellOutput.println("export-type:png");
        shellOutput.println("export-dpi:450");
        shellOutput.println("export-do");
        LOG.info("Export actions was enter for file {}", targetFile);
        if (!listener.accept()) {
            LOG.error("Accept Timeout exceeded for file: {}", targetFile);
        }
        shellOutput.println("file-close");
    }

    private Process enterToInkscapeShell() {
        ProcessBuilder inkscapeBuilder = new ProcessBuilder("inkscape", "--shell");
        redirectOutput(inkscapeBuilder);
        redirectErrors(inkscapeBuilder);
        try {
            return inkscapeBuilder.start();
        } catch (IOException exception) {
            throw new RuntimeException("Failed enter to inkscape shell", exception);
        }
    }

    private void redirectErrors(ProcessBuilder inkscapeBuilder) {
        if (logsDir != null) {
            try {
                logsDir.mkdirs();
                inkscapeBuilder.redirectError(createConversionErrorLog(logsDir));
            } catch (IOException e) {
                LOG.warn("Redirect inkscape shell errors to file failed: ", e);
                inkscapeBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);
            }
        } else {
            inkscapeBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);
        }
    }

    private void redirectOutput(ProcessBuilder inkscapeBuilder) {
        if (logsDir != null) {
            try {
                logsDir.mkdirs();
                inkscapeBuilder.redirectOutput(createConversionOutputLog(logsDir));
            } catch (IOException e) {
                LOG.warn("Redirect inkscape shell output to file failed: ", e);
                inkscapeBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            }
        } else {
            inkscapeBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        }
    }

    private File createConversionErrorLog(File conversionLogsDir) throws IOException {
        File errorLog = new File(conversionLogsDir, "error.log");
        if (!errorLog.exists()) {
            errorLog.createNewFile();
        }
        return errorLog;
    }

    private File createConversionOutputLog(File conversionLogsDir) throws IOException {
        File errorLog = new File(conversionLogsDir, "input.log");
        if (!errorLog.exists()) {
            errorLog.createNewFile();
        }
        return errorLog;
    }

    private String getCanonicalPath(File sourceSvgFile) {
        try {
            return sourceSvgFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        shellOutput.println("quit");
        shellProcess.destroy();
        LOG.info("Exit from Inkscape Shell accepted");
    }
}
