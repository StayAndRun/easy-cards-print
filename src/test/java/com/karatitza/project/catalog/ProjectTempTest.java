package com.karatitza.project.catalog;

import com.karatitza.converters.ConversionTask;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.io.File;

public class ProjectTempTest {

    protected void cleanTempDirectory(String projectPath) {
        File tempDir = new File(projectPath + ConversionTask.TEMP_FILES_RELATE_PATH);
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
