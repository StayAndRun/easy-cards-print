package com.karatitza.converters.inkscape.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class InkscapeShell implements AutoCloseable {
    public static final Logger LOG = LoggerFactory.getLogger(InkscapeShell.class);

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
        shellOutput.println("file-open:" + getCanonicalPath(sourceFile));
        shellOutput.println("export-filename:" + getCanonicalPath(targetFile));
        shellOutput.println("export-pdf-version:1.5");
        shellOutput.println("export-type:pdf");
        shellOutput.println("export-do");
//        shellOutput.println("file-close");
        LOG.info("Export actions was enter for file {}", targetFile);
    }

    private void logFileExists(File targetFile) {
        try {
            LOG.info("Exists: {}", targetFile.exists());
            LOG.info("Exists: {} (Absolute)", targetFile.getAbsoluteFile().exists());
            LOG.info("Exists: {} (Canonical)", targetFile.getCanonicalFile().exists());
            LOG.info("Can read: {}", targetFile.canRead());
            LOG.info("Can read: {} (Absolute)", targetFile.getAbsoluteFile().canRead());
            LOG.info("Can read: {} (Canonical)", targetFile.getCanonicalFile().canRead());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public File createConversionErrorLog(File conversionLogsDir) throws IOException {
        File errorLog = new File(conversionLogsDir, "error.log");
        if (!errorLog.exists()) {
            errorLog.createNewFile();
        }
        return errorLog;
    }

    public File createConversionOutputLog(File conversionLogsDir) throws IOException {
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
