package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.PdfPagesComposer;
import com.karatitza.project.PdfPagesComposerByIText;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DocumentLayoutTest {

    public static final String PDF_CATALOG_PATH = "C:/GitRepo/EasyCardsPrint/src/test/resources/testProject/print/temp/PDF";
    public static final String PROJECT_PATH = "C:/GitRepo/EasyCardsPrint/src/test/resources/testProject";

    @Test
    void acceptPdfPagesCompile() {
        LayoutComposer composer = createComposer(PageSize.A4, SpotSize.millimeters(100, 70));
        DecksCatalog pdfCatalog = new DecksCatalog(new File(PDF_CATALOG_PATH), ImageFormat.PDF);
        DocumentLayout pages = composer.compose(pdfCatalog);
        PdfPagesComposer pdfPagesComposer = createPdfPagesLayout();
        pdfPagesComposer.composeByLayout(pages);
    }

    private PdfPagesComposer createPdfPagesLayout() {
        return new PdfPagesComposerByIText(new File(PROJECT_PATH));
    }

    private LayoutComposer createComposer(PageSize pageSize, SpotSize millimeters) {
        return new LayoutComposer(pageSize, millimeters);
    }
}
