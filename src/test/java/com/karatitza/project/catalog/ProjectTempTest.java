package com.karatitza.project.catalog;

import org.junit.jupiter.api.extension.TestInstantiationException;

import java.io.File;

import static java.text.MessageFormat.format;

public class ProjectTempTest {

    public static final String TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);

    protected void cleanTempDirectory(String projectPath) {
        File tempDir = new File(projectPath + TEMP_FILES_RELATE_PATH);
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
                subFile.delete();
            }
        }
    }
}
