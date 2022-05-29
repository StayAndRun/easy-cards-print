package com.karatitza.project.catalog;

import org.junit.jupiter.api.extension.TestInstantiationException;

import java.io.File;

import static java.text.MessageFormat.format;

public class TempFilesTest {

    public static final String PROJECT_TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);
    public static final String TEMP_FILES_PATH = File.separator + "temp";

    protected void cleanupProjectTempFiles(String projectPath) {
        File tempDir = new File(projectPath + PROJECT_TEMP_FILES_RELATE_PATH);
        deleteSubDirs(tempDir);
        if (tempDir.exists() && !tempDir.delete()) {
            throw new TestInstantiationException("Failed to remove temp files: " + tempDir);
        }
    }

    protected void cleanupTempFiles(String testDir) {
        File tempDir = new File(testDir + TEMP_FILES_PATH);
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
