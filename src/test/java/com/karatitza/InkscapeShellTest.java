package com.karatitza;

import com.karatitza.converters.inkscape.console.InkscapeShell;
import com.karatitza.project.catalog.TempFilesTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class InkscapeShellTest extends TempFilesTest {
    public static final Logger LOG = LoggerFactory.getLogger(InkscapeShellTest.class);

    private static final String SOURCE_SIMPLE_SVG_DIR = "./src/test/resources/conversion/source/svg-100/";
    private static final String SOURCE_KDM_SVG_DIR = "./src/test/resources/conversion/source/kdm-svg/";
    private static final String TEMP_PDF_DIR = "./src/test/resources/conversion/temp/pdf/";
    private static final String TEST_FILES_DIR = "./src/test/resources/conversion";

    @Test
    void acceptFileExportToPdf() {
        Map<File, File> testFiles = testFiles(SOURCE_SIMPLE_SVG_DIR);
        try (InkscapeShell inkscapeShell = new InkscapeShell(new File(TEMP_PDF_DIR).getParentFile())) {
            for (Map.Entry<File, File> entry : testFiles.entrySet()) {
                File key = entry.getKey();
                File value = entry.getValue();
                inkscapeShell.exportToPdfFile(key, value);
            }
        }
        testFiles.forEach((source, target) -> assertTargetFileExists(target));
    }

    private Map<File, File> testFiles(String filesDir) {
        return Arrays.stream(new File(filesDir).listFiles()).filter(File::isFile)
                .collect(Collectors.toMap(file -> file, this::buildPdfFile));

    }

    private File buildPdfFile(File file) {
        return new File(TEMP_PDF_DIR + file.getName().replace(".svg", ".pdf"));
    }

    private void assertTargetFileExists(File targetFile) {
        try {
            LOG.info("Exists: {}", targetFile.exists());
            LOG.info("Exists: {} (Absolute)", targetFile.getAbsoluteFile().exists());
            LOG.info("Exists: {} (Canonical)", targetFile.getCanonicalFile().exists());
            LOG.info("Can read: {}", targetFile.canRead());
            LOG.info("Can read: {} (Absolute)", targetFile.getAbsoluteFile().canRead());
            LOG.info("Can read: {} (Canonical)", targetFile.getCanonicalFile().canRead());
            Assertions.assertTrue(targetFile.exists(), "Not exists file: " + targetFile);
        } catch (IOException e) {
            Assertions.fail("Failed assert: ", e);
        }
    }

    @BeforeEach
    void setUp() {
        cleanupTempFiles(TEST_FILES_DIR);
    }
}
