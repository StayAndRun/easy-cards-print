package com.karatitza.project;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.utils.CompareTool;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.catalog.ProjectTempTest;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ProjectBuildPdfTest extends ProjectTempTest {

    public static final String PDF_PROJECT_PATH = "./src/test/resources/pdf-project";
    public static final String SVG_PROJECT_PATH = "./src/test/resources/svg-project";

    @BeforeEach
    void setUp() {
        cleanTempDirectory(PDF_PROJECT_PATH);
    }

    @Test
    void acceptBuildFromPdfCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(PDF_PROJECT_PATH), ImageFormat.PDF);
        cardProject.defineSpots(PageSize.A4, SpotSize.millimeters(91, 59));
        cardProject.buildFinalPdf();
        File actualFile = searchTempPdfFile("pdf-project.pdf", PDF_PROJECT_PATH);
        assertPdfFilesEquals(
                "./src/test/resources/pdf-project/expected/spot-91x59/pdf-project.pdf", actualFile.getPath()
        );
    }

    @Test
    void acceptBuildFromSvgCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SVG_PROJECT_PATH), ImageFormat.SVG);
        cardProject.defineSpots(PageSize.A4, SpotSize.millimeters(91, 59, 5));
        cardProject.buildFinalPdf();
        //TODO assert files
    }

    private File searchTempPdfFile(String expectedFileName, String projectDir) {
        File projectRoot = new File(projectDir);
        File printDir = Arrays.stream(Objects.requireNonNull(projectRoot.listFiles((dir, name) -> "print".equals(name))))
                .findFirst().orElseThrow(() -> new AssertionFailedError("Not found 'print' dir at: " + projectRoot.getName()));
        File[] tempFiles = Objects.requireNonNull(printDir.listFiles());
        Optional<File> foundFile = Arrays.stream(tempFiles)
                .filter(file -> file.getName().equals(expectedFileName)).findFirst();
        if (!foundFile.isPresent()) {
            Assertions.fail("Not found expected file: " + expectedFileName);
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
