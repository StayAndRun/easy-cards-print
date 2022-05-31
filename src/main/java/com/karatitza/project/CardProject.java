package com.karatitza.project;

import com.google.gson.Gson;
import com.karatitza.converters.ConversionFactory;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

public class CardProject {
    public static final Logger LOG = LoggerFactory.getLogger(CardProject.class);
    public static final String DECKS_DIR_NAME = "decks";

    private DecksCatalog selectedCatalog = defaultEmptyCatalog();
    private SpotsLayout spotsLayout = defaultSpotLayout();
    private ConversionFactory conversionFactory = defaultConversionFactory();
    private File projectRoot = loadLatestRoot();

    public DecksCatalog getSelectedCatalog() {
        return selectedCatalog;
    }

    public File getProjectRoot() {
        return projectRoot;
    }

    public DecksCatalog selectCatalog(File projectRoot, ImageFormat format) {
        this.projectRoot = projectRoot;
        this.selectedCatalog = new DecksCatalog(selectDecksDir(projectRoot), format);
        LOG.info("Selected Catalog: " + selectedCatalog);
        saveLatestProjectConfiguration(projectRoot);
        return selectedCatalog;
    }

    public void selectConverterFactory(ConversionFactory factory) {
        this.conversionFactory = factory;
        LOG.info("Selected converter factory: " + factory);
    }

    public SpotsPreview defineSpots(PageFormat pageFormat, SpotSize spotSize) {
        this.spotsLayout = new SpotsLayout(pageFormat, spotSize);
        return new SpotsPreview(spotsLayout);
    }
    
    public CardProjectSnapshot snapshot() {
        return new CardProjectSnapshot(
                getCurrentProjectPath(), spotsLayout, getSelectedCatalog(), conversionFactory
        );
    }

    private File getCurrentProjectPath() {
        if (projectRoot == null) {
            throw new UnsupportedOperationException("Default path not specified");
        } else {
            return projectRoot;
        }
    }

    private File selectDecksDir(File projectRootDir) {
        return Arrays.stream(requireNonNull(projectRootDir.listFiles((dir, name) -> DECKS_DIR_NAME.equals(name))))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found source dir at project: " + projectRootDir.getName()));
    }

    private static void saveLatestProjectConfiguration(File projectRoot) {
        Gson gson = new Gson();
        File confFile = new File("./latest/conf.json");
        confFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(confFile)) {
            confFile.createNewFile();
            gson.toJson(new LatestProjectConfiguration(projectRoot.getCanonicalPath()), writer);
        } catch (IOException e) {
            LOG.warn("Failed save latest project configuration.", e);
        }
    }

    private static ConversionFactory defaultConversionFactory() {
        return new ConversionFactory.ITextConversionFactory();
    }

    private static SpotsLayout defaultSpotLayout() {
        return new SpotsLayout(CommonPageFormat.A4, SpotSize.millimeters(92, 59));
    }

    private static DecksCatalog defaultEmptyCatalog() {
        return new DecksCatalog(Collections.emptyList(), ImageFormat.PDF);
    }

    private static File loadLatestRoot() {
        File file = new File("./latest/conf.json");
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(file)) {
            LatestProjectConfiguration configuration = gson.fromJson(fileReader, LatestProjectConfiguration.class);
            File latestRoot = new File(configuration.getProjectRoot());
            LOG.info("Found latest root: {}", latestRoot);
            return latestRoot;
        } catch (FileNotFoundException e) {
            LOG.info("Not found latest project config");
            return new File("");
        } catch (IOException e) {
            LOG.warn("Failed to load latest project config");
            return new File("");
        }
    }
}
