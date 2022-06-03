package com.karatitza.project.catalog;

import com.karatitza.project.LatestProjectsConfig;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.io.File;

import static java.text.MessageFormat.format;

public class TempFilesTest {

    public static final String PROJECT_TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);
    public static final String PROJECT_PRINT_FILES_RELATE_PATH = format("{0}print", File.separator);
    public static final String TEMP_FILES_PATH = File.separator + "temp";
    public static final String PROJECT_LATEST_CONFIG_PATH = File.separator + ".latest";
    public static final String PROJECT_CONFIG_JSON = "config.json";

    protected void cleanupProjectTempFiles(String projectPath) {
        cleanupPath(new File(projectPath + PROJECT_TEMP_FILES_RELATE_PATH));
    }

    protected void cleanupProjectPrintFiles(String projectPath) {
        cleanupPath(new File(projectPath + PROJECT_PRINT_FILES_RELATE_PATH));
    }

    protected void cleanupTempFiles(String testDir) {
        cleanupPath(new File(testDir + TEMP_FILES_PATH));
    }

    protected void cleanupProjectConfigFile(String projectRoot) {
        cleanupPath(new File(projectRoot, PROJECT_CONFIG_JSON));
    }

    protected void cleanupLatestProjectsFile() {
        cleanupPath(new File(LatestProjectsConfig.CONFIG_FILE_NAME));
    }

    private void cleanupPath(File dir) {
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
