package com.karatitza.project;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class CardProject {
    public static final Logger LOG = LoggerFactory.getLogger(CardProject.class);
    public static final String DECKS_DIR_NAME = "decks";

    private DecksCatalog selectedCatalog = DecksCatalog.empty();
    private SpotsLayout spotsLayout = defaultSpotLayout();
    private ConversionFactory conversionFactory = defaultConversionFactory();
    private File projectRoot = new File("");

    public static CardProject openFromDir(File root) {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(root);
        CardProjectConfig.loadFromFile(new File(root, CardProjectConfig.CONFIG_FILE_NAME))
                .ifPresent(config -> cardProject.selectSpots(config.getPageFormat(), config.getSpotSize()));
        return cardProject;
    }

    public static CardProject openFromConfiguration(CardProjectConfig config) {
        CardProject cardProject = new CardProject();
        cardProject.selectCatalog(config.getProjectRoot());
        cardProject.selectSpots(
                config.getPageFormat(),
                config.getSpotSize());
        return cardProject;
    }

    public DecksCatalog getSelectedCatalog() {
        return selectedCatalog;
    }

    public SpotsLayout getSpotsLayout() {
        return spotsLayout;
    }

    public File getProjectRoot() {
        return projectRoot;
    }

    public DecksCatalog selectCatalog(File projectRoot) {
        this.projectRoot = projectRoot;
        this.selectedCatalog = selectCatalogDir(projectRoot).map(DecksCatalog::new).orElse(DecksCatalog.empty());
        LOG.info("Selected Catalog: " + selectedCatalog);
        return selectedCatalog;
    }

    public void selectConverterFactory(ConversionFactory factory) {
        this.conversionFactory = factory;
        LOG.info("Selected converter factory: " + factory);
    }

    public SpotsPreview selectSpots(PageFormat pageFormat, SpotSize spotSize) {
        LOG.info("Selected spots: {}, format: {}", spotSize, pageFormat);
        this.spotsLayout = new SpotsLayout(pageFormat, spotSize);
        return new SpotsPreview(spotsLayout);
    }

    public CardProjectSnapshot snapshot() {
        saveProjectConfig();
        return new CardProjectSnapshot(
                getCurrentProjectPath(), spotsLayout, getSelectedCatalog(), conversionFactory
        );
    }

    private void saveProjectConfig() {
        try {
            CardProjectConfig config = new CardProjectConfig(
                    projectRoot, spotsLayout.getSpotSize(), spotsLayout.getPageFormat()
            );
            config.saveToFile();
        } catch (Exception e) {
            LOG.warn("Failed to save project configuration.", e);
        }
    }

    private File getCurrentProjectPath() {
        if (projectRoot == null) {
            throw new UnsupportedOperationException("Default path not specified");
        } else {
            return projectRoot;
        }
    }

    private Optional<File> selectCatalogDir(File projectRootDir) {
        return Arrays.stream(requireNonNull(projectRootDir.listFiles((dir, name) -> DECKS_DIR_NAME.equals(name))))
                .findFirst();
    }

    private static ConversionFactory defaultConversionFactory() {
        return new ConversionFactory.ITextConversionFactory();
    }

    private static SpotsLayout defaultSpotLayout() {
        return new SpotsLayout(CommonPageFormat.A4, SpotSize.millimeters(92, 59));
    }

}
