package com.karatitza.project;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.PdfDocumentComposer;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.LayoutComposer;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

public class CardProject {
    public static final Logger LOG = LoggerFactory.getLogger(CardProject.class);
    public static final String DECKS_DIR_NAME = "decks";

    private DecksCatalog selectedCatalog = defaultEmptyCatalog();
    private SpotsLayout spotsLayout = defaultSpotLayout();
    private ConversionFactory conversionFactory = defaultConversionFactory();
    private File projectRoot;

    public DecksCatalog selectCatalog(File projectRoot, ImageFormat format) {
        this.projectRoot = projectRoot;
        this.selectedCatalog = new DecksCatalog(selectDecksDir(projectRoot), format);
        LOG.info("Selected Catalog: " + selectedCatalog);
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

    public File buildFinalPdf() {
        PdfDocumentComposer pdfDocumentComposer = new PdfDocumentComposer(getCurrentProjectPath());
        LayoutComposer layoutComposer = new LayoutComposer(new DocumentLayout(spotsLayout));
        DocumentLayout composedDocumentLayout = layoutComposer.compose(prepareCatalog());
        return pdfDocumentComposer.compose(composedDocumentLayout);
    }

    public DecksCatalog getSelectedCatalog() {
        return selectedCatalog;
    }

    private DecksCatalog prepareCatalog() {
        DecksCatalog currentCatalog = selectedCatalog;
        if (currentCatalog.getImageFormat() != ImageFormat.PDF) {
            ImageConverter converter = conversionFactory.create(new TempImageFactory(projectRoot));
            currentCatalog = selectedCatalog.convert(converter);
        }
        return currentCatalog;
    }

    private File getCurrentProjectPath() {
        if (projectRoot == null) {
            return new File("./src/test/resources/pdf-project");
        } else {
            return projectRoot;
        }
    }

    private File selectDecksDir(File projectRootDir) {
        return Arrays.stream(requireNonNull(projectRootDir.listFiles((dir, name) -> DECKS_DIR_NAME.equals(name))))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found source dir at project: " + projectRootDir.getName()));
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
}
