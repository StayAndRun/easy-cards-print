package com.karatitza.project;

import com.itextpdf.kernel.utils.CompareTool;
import com.karatitza.TempFilesTest;
import com.karatitza.converters.ConversionFactory;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ProjectBuildPdfTest extends TempFilesTest {

    public static final String PDF_PROJECT_PATH = "./src/test/resources/pdf-project";
    public static final String SVG_PROJECT_PATH = "./src/test/resources/svg-project";
    public static final String PNG_PROJECT_PATH = "./src/test/resources/png-project";
    public static final String SVG_PROJECT_WITH_TEMP_PATH = "./src/test/resources/svg-project";
    public static final String EXPECTED_PROJECT_PDF = "./src/test/resources/expected/spot-91x59/expected-project.pdf";
    public static final String EXPECTED_PNG_PROJECT_PDF = "./src/test/resources/expected/spot-91x59/png-project(A4-91x59-0).pdf";
    public static final String EXPECTED_PROJECT_INKSCAPE_PDF = "./src/test/resources/expected/spot-91x59/expected-project-inkscape.pdf";

    @BeforeEach
    void setUp() {
        cleanupProjectPrintFiles(PDF_PROJECT_PATH);
        cleanupProjectPrintFiles(SVG_PROJECT_PATH);
    }

    @Test
    void acceptBuildFromPdfCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(PDF_PROJECT_PATH));
        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(91, 59));
        cardProject.snapshot().buildFinalPdf();
        File actualFile = searchTempPdfFile("pdf-project(A4-91x59-0).pdf", PDF_PROJECT_PATH);
        assertPdfFilesEquals(EXPECTED_PROJECT_PDF, actualFile.getPath());
    }

    @Test
    void acceptBuildFromSvgCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SVG_PROJECT_PATH));
        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(91, 59));
        cardProject.snapshot().buildFinalPdf(progress -> System.out.println("Progress percentage: " + progress));
        File actualFile = searchTempPdfFile("svg-project(A4-91x59-0).pdf", SVG_PROJECT_PATH);
        assertPdfFilesEquals(EXPECTED_PROJECT_PDF, actualFile.getPath());
    }

    @Test
    void acceptBuildFromPngCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(PNG_PROJECT_PATH));
        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(91, 59));
        cardProject.snapshot().buildFinalPdf(progress -> System.out.println("Progress percentage: " + progress));
        File actualFile = searchTempPdfFile("png-project(A4-91x59-0).pdf", PNG_PROJECT_PATH);
        assertPdfFilesEquals(EXPECTED_PNG_PROJECT_PDF, actualFile.getPath());
    }

    @Test
    void acceptBuildFromSvgCatalogWithInkscapeConversion() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SVG_PROJECT_PATH));
        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(91, 59));
        cardProject.selectConverterFactory(new ConversionFactory.InkscapePdfConversionFactory());
        cardProject.snapshot().buildFinalPdf(progress -> System.out.println("Progress percentage: " + progress));
        File actualFile = searchTempPdfFile("svg-project(A4-91x59-0).pdf", SVG_PROJECT_PATH);
        assertPdfFilesEquals(EXPECTED_PROJECT_INKSCAPE_PDF, actualFile.getPath());
    }

    @Test
    void acceptBuildFromSvgCatalogWithExistingTempFiles() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SVG_PROJECT_PATH));
        cardProject.selectConverterFactory(new ConversionFactory.InkscapePdfConversionFactory());
        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(91, 59));

        cardProject.snapshot().buildFinalPdf(progress -> System.out.println("Progress percentage: " + progress));
        searchTempPdfFile("svg-project(A4-91x59-0).pdf", SVG_PROJECT_PATH);

        cardProject.selectSpots(CommonPageFormat.A4, SpotSize.millimeters(89, 57));
        cardProject.snapshot().buildFinalPdf(progress -> System.out.println("Progress percentage: " + progress));
        searchTempPdfFile("svg-project(A4-89x57-0).pdf", SVG_PROJECT_PATH);
    }

    private File searchTempPdfFile(String expectedFileName, String projectDir) {
        File projectRoot = new File(projectDir);
        File printDir = Arrays.stream(Objects.requireNonNull(projectRoot.listFiles((dir, name) -> "print".equals(name))))
                .findFirst().orElseThrow(() -> new AssertionFailedError("Not found 'print' dir at: " + projectRoot.getName()));
        File[] tempFiles = Objects.requireNonNull(printDir.listFiles());
        Optional<File> foundFile = Arrays.stream(tempFiles)
                .filter(file -> file.getName().equals(expectedFileName)).findFirst();
        if (foundFile.isEmpty()) {
            Assertions.fail("Not found expected file: " + expectedFileName + ", but found: " + Arrays.toString(tempFiles));
        }
        return foundFile.get();
    }

    private void assertPdfFilesEquals(String expectedPdf, String actualPdf) {
        CompareTool compareTool = new CompareTool();
        try {
            String result = compareTool.compareByContent(actualPdf, expectedPdf, "/src/test/resources/");
            Assertions.assertNull(result, "Compare pdf content result: " + result);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }
}
