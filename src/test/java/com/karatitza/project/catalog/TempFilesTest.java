package com.karatitza.project.catalog;

import org.junit.jupiter.api.extension.TestInstantiationException;

import java.io.File;

import static java.text.MessageFormat.format;

public class TempFilesTest {

    public static final String PROJECT_TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);
    public static final String PROJECT_PRINT_FILES_RELATE_PATH = format("{0}print", File.separator);
    public static final String TEMP_FILES_PATH = File.separator + "temp";

    protected void cleanupProjectTempFiles(String projectPath) {
        cleanupDirectory(new File(projectPath + PROJECT_TEMP_FILES_RELATE_PATH));
    }

    protected void cleanupProjectPrintFiles(String projectPath) {
        cleanupDirectory(new File(projectPath + PROJECT_PRINT_FILES_RELATE_PATH));
    }

    protected void cleanupTempFiles(String testDir) {
        cleanupDirectory(new File(testDir + TEMP_FILES_PATH));
    }

    private void cleanupDirectory(File dir) {
        File tempDir = dir;
        deleteSubDirs(tempDir);
        if (tempDir.exists() && !tempDir.delete()) {
            throw new TestInstantiationException("Failed to remove temp files: " + tempDir);
        }
    }

    private void deleteSubDirs(File tempDir) {
        File[] subFiles = tempDir.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                deleteSubDirs(subFile);
                if (!subFile.delete()) {
                    throw new TestInstantiationException("Failed to remove temp file: " + subFile);
                }
            }
        }
    }
}
