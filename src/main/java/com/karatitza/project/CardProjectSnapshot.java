package com.karatitza.project;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempFileProvider;
import com.karatitza.exceptions.FinalPdfBuildException;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.PdfDocumentComposer;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.LayoutComposer;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Consumer;

public class CardProjectSnapshot {
    public static final Logger LOG = LoggerFactory.getLogger(CardProjectSnapshot.class);

    private final File projectRoot;
    private final SpotsLayout spots;
    private final DecksCatalog selectedCatalog;
    private final ImageConverter converter;
    private final boolean enableBacks;

    public CardProjectSnapshot(
            File projectRoot, SpotsLayout spots, DecksCatalog catalog, ConversionFactory conversionFactory, boolean enableBacks) {
        this.projectRoot = projectRoot;
        this.spots = spots;
        this.selectedCatalog = catalog.selectedCatalog();
        this.enableBacks = enableBacks;
        converter = conversionFactory.create(new TempFileProvider(projectRoot));

    }

    public File buildFinalPdf() {
        return buildFinalPdf(prepareCatalog());
    }

    public File buildFinalPdf(Consumer<Integer> progressConsumer) {
        return buildFinalPdf(prepareCatalog(progressConsumer));
    }

    private File buildFinalPdf(DecksCatalog progressConsumer) {
        try {
            return tryToBuildFinalPdf(progressConsumer);
        } catch (Exception exception) {
            LOG.error("Failed build final PDF: {}", exception.getMessage());
            throw new FinalPdfBuildException(exception);
        }
    }

    private File tryToBuildFinalPdf(DecksCatalog decksCatalog) {
        PdfDocumentComposer pdfDocumentComposer = new PdfDocumentComposer(projectRoot);
        LayoutComposer layoutComposer = new LayoutComposer(new DocumentLayout(spots));
        DocumentLayout composedDocumentLayout = layoutComposer.compose(decksCatalog);
        composedDocumentLayout.disableBacks(enableBacks);
        return pdfDocumentComposer.compose(composedDocumentLayout);
    }

    private DecksCatalog prepareCatalog() {
        return selectedCatalog.convert(converter);
    }

    private DecksCatalog prepareCatalog(Consumer<Integer> progressConsumer) {
        if (isSvgConversionNeed()) {
            return selectedCatalog.convert(converter, progressConsumer);
        } else {
            return selectedCatalog;
        }
    }

    private boolean isSvgConversionNeed() {
        return selectedCatalog.getCatalogStatistic().countImagesByFormat(ImageFormat.SVG) != 0;
    }
}
