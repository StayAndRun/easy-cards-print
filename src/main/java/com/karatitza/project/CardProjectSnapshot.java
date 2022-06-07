package com.karatitza.project;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempFileProvider;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.PdfDocumentComposer;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.LayoutComposer;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.io.File;
import java.util.function.Consumer;

public class CardProjectSnapshot {
    private final File projectRoot;
    private final SpotsLayout spots;
    private final DecksCatalog catalog;
    private final ImageConverter converter;

    public CardProjectSnapshot(File projectRoot, SpotsLayout spots, DecksCatalog catalog, ConversionFactory conversionFactory) {
        this.projectRoot = projectRoot;
        this.spots = spots;
        this.catalog = catalog;
        converter = conversionFactory.create(new TempFileProvider(projectRoot));
    }

    public File buildFinalPdf() {
        PdfDocumentComposer pdfDocumentComposer = new PdfDocumentComposer(projectRoot);
        LayoutComposer layoutComposer = new LayoutComposer(new DocumentLayout(spots));
        DocumentLayout composedDocumentLayout = layoutComposer.compose(prepareCatalog());
        return pdfDocumentComposer.compose(composedDocumentLayout);
    }

    public File buildFinalPdf(Consumer<Integer> progressConsumer) {
        PdfDocumentComposer pdfDocumentComposer = new PdfDocumentComposer(projectRoot);
        LayoutComposer layoutComposer = new LayoutComposer(new DocumentLayout(spots));
        DocumentLayout composedDocumentLayout = layoutComposer.compose(prepareCatalog(progressConsumer));
        return pdfDocumentComposer.compose(composedDocumentLayout);
    }

    private DecksCatalog prepareCatalog() {
        return catalog.convert(converter);
    }

    private DecksCatalog prepareCatalog(Consumer<Integer> progressConsumer) {
        int svgCount = catalog.getCatalogStatistic().countImagesByFormat(ImageFormat.SVG);
        if (svgCount == 0) {
            return catalog;
        } else {
            return catalog.convert(converter, progressConsumer);
        }
    }
}
