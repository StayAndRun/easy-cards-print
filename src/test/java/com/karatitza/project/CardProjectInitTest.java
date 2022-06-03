package com.karatitza.project;

import com.karatitza.project.catalog.TempFilesTest;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class CardProjectInitTest extends TempFilesTest {

    public static final String SRC_TEST_RESOURCES_SVG_PROJECT = "./src/test/resources/svg-project";
    public static final String SRC_TEST_RESOURCES_PDF_PROJECT = "./src/test/resources/pdf-project";

    @BeforeEach
    void setUp() {
        cleanupProjectConfigFile(SRC_TEST_RESOURCES_SVG_PROJECT);
        cleanupProjectConfigFile(SRC_TEST_RESOURCES_PDF_PROJECT);
        cleanupLatestProjectsFile();
    }

    @Test
    void acceptProjectConfigSavedToFile() {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SRC_TEST_RESOURCES_SVG_PROJECT));
        cardProject.selectSpots(CommonPageFormat.A1, SpotSize.points(50, 40, 10));
        cardProject.snapshot();
        CardProjectConfig configuration = CardProjectConfig.loadFromFile(
                new File(SRC_TEST_RESOURCES_SVG_PROJECT, "config.json")).orElseThrow();
        Assertions.assertEquals(50, configuration.getSpotSize().getHeight());
        Assertions.assertEquals(40, configuration.getSpotSize().getWidth());
        Assertions.assertEquals(10, configuration.getSpotSize().getSpace());
        Assertions.assertEquals(CommonPageFormat.A1, configuration.getPageFormat());
    }

    @Test
    void acceptCreateProjectFromConfigFile() throws IOException {
        CardProjectConfig cardProjectConfig = new CardProjectConfig(
                new File(SRC_TEST_RESOURCES_SVG_PROJECT), 20, 30, 10, CommonPageFormat.A2
        );
        cardProjectConfig.saveToFile();
        CardProject cardProject = CardProject.openFromDir(new File(SRC_TEST_RESOURCES_SVG_PROJECT));
        SpotSize spotSize = cardProject.getSpotsLayout().getSpotSize();
        Assertions.assertEquals(30, spotSize.getHeight());
        Assertions.assertEquals(20, spotSize.getWidth());
        Assertions.assertEquals(10, spotSize.getSpace());
        Assertions.assertEquals(CommonPageFormat.A2, cardProject.getSpotsLayout().getPageFormat());
    }

    @Test
    void acceptLatestProjectSavedToFile() throws IOException {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SRC_TEST_RESOURCES_SVG_PROJECT));
        cardProject.snapshot();

        String expectedPath = new File(SRC_TEST_RESOURCES_SVG_PROJECT).getCanonicalPath();
        LatestProjectsConfig config = LatestProjectsConfig.loadFromFile();
        Assertions.assertEquals(1, config.getLatestProjects().size());
        Assertions.assertEquals(expectedPath, config.getLatestProjects().get(0).getCanonicalPath());
    }

    @Test
    void acceptMultipleLatestProjectsSavedToFile() throws IOException {
        CardProject svgProject = CardProject.openFromDir(new File(SRC_TEST_RESOURCES_SVG_PROJECT));
        svgProject.snapshot();
        CardProject pdfProject = CardProject.openFromDir(new File(SRC_TEST_RESOURCES_PDF_PROJECT));
        pdfProject.snapshot();

        String svgExpectedPath = new File(SRC_TEST_RESOURCES_SVG_PROJECT).getCanonicalPath();
        String pdfExpectedPath = new File(SRC_TEST_RESOURCES_PDF_PROJECT).getCanonicalPath();
        LatestProjectsConfig config = LatestProjectsConfig.loadFromFile();
        Assertions.assertEquals(2, config.getLatestProjects().size());
        Assertions.assertEquals(svgExpectedPath, config.getLatestProjects().get(0).getCanonicalPath());
        Assertions.assertEquals(pdfExpectedPath, config.getLatestProjects().get(1).getCanonicalPath());
    }

    @Test
    void rejectLatestProjectSaveIfAlreadySaved() throws IOException {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(new File(SRC_TEST_RESOURCES_SVG_PROJECT));
        cardProject.snapshot();
        cardProject.snapshot();

        String expectedPath = new File(SRC_TEST_RESOURCES_SVG_PROJECT).getCanonicalPath();
        LatestProjectsConfig config = LatestProjectsConfig.loadFromFile();
        Assertions.assertEquals(1, config.getLatestProjects().size());
        Assertions.assertEquals(expectedPath, config.getLatestProjects().get(0).getCanonicalPath());
    }
}
