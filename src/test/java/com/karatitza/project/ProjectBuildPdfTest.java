package com.karatitza.project;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.catalog.ProjectTempTest;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

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
        cardProject.defineSpots(PageSize.A4, SpotSize.millimeters(91, 59, 5));
        cardProject.buildFinalPdf();
        //TODO assert files
    }

    @Test
    void acceptBuildFromSvgCatalog() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SVG_PROJECT_PATH), ImageFormat.SVG);
        cardProject.defineSpots(PageSize.A4, SpotSize.millimeters(91, 59, 5));
        cardProject.buildFinalPdf();
        //TODO assert files
    }
}
