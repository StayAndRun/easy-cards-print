package com.karatitza;

import com.itextpdf.kernel.utils.CompareTool;
import com.karatitza.converters.inkscape.console.InkscapeShell;
import com.karatitza.project.catalog.TempFilesTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    void acceptHundredFilesExportToPdf() {
        Map<File, File> testFiles = testFiles(SOURCE_SIMPLE_SVG_DIR);
        try (InkscapeShell inkscapeShell = new InkscapeShell(new File(TEMP_PDF_DIR).getParentFile())) {
            for (Map.Entry<File, File> entry : testFiles.entrySet()) {
                File key = entry.getKey();
                File value = entry.getValue();
                inkscapeShell.exportToPdfFile(key, value);
            }
        }
        testFiles.forEach((source, target) -> Assertions.assertTrue(
                target.exists(), "Not exists file: " + target));
        testFiles.forEach((source, target) -> assertPdfFilesEquals(
                "./src/test/resources/expected/skeleton.pdf", target.getPath()));
    }

    @Test
    @Disabled
    void acceptHugeFileExportToPdf() {
        File sourceFile = new File("./src/test/resources/conversion/source/svg-huge/back.svg");
        File targetFile = buildTempPdfFile(sourceFile);
        try (InkscapeShell inkscapeShell = new InkscapeShell(new File(TEMP_PDF_DIR).getParentFile())) {
            inkscapeShell.exportToPdfFile(sourceFile, targetFile);
        }
        Assertions.assertTrue(targetFile.exists(), "File should exists: " + targetFile);
        Assertions.assertTrue(targetFile.canRead(), "File should can read: " + targetFile);
        assertPdfFilesEquals("./src/test/resources/expected/back.pdf", targetFile.getPath());
    }

    private void assertPdfFilesEquals(String expectedPdf, String actualPdf) {
        CompareTool compareTool = new CompareTool();
        try {
            String result = compareTool.compareByContent(actualPdf, expectedPdf, TEMP_PDF_DIR);
            Assertions.assertNull(result, "Compare pdf content result: " + result);
        } catch (Exception e) {
            Assertions.fail("Files compare failed: " + actualPdf, e);
        }
    }

    private Map<File, File> testFiles(String filesDir) {
        return Arrays.stream(new File(filesDir).listFiles()).filter(File::isFile)
                .collect(Collectors.toMap(file -> file, this::buildTempPdfFile));

    }

    private File buildTempPdfFile(File file) {
        return new File(TEMP_PDF_DIR + file.getName().replace(".svg", ".pdf"));
    }

    @BeforeEach
    void setUp() {
        cleanupTempFiles(TEST_FILES_DIR);
    }
}
