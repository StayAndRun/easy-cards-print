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

    @BeforeEach
    void setUp() {
        cleanupProjectConfigFile(SRC_TEST_RESOURCES_SVG_PROJECT);
    }

    @Test
    void acceptProjectConfigSavedToFile() throws IOException {
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
    void acceptCreateProjectFromConfigFile() throws IOException, InterruptedException {
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
}
