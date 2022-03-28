package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.utils.CompareTool;
import com.karatitza.project.PdfPagesComposer;
import com.karatitza.project.PdfPagesComposerByIText;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.catalog.ProjectTempTest;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DocumentLayoutTest extends ProjectTempTest {

    public static final String PDF_CATALOG_PATH = "./src/test/resources/pdf-project/source";
    public static final String PDF_PROJECT_PATH = "./src/test/resources/pdf-project";
    public static final String PROJECT_TEMP_PATH = "./src/test/resources/pdf-project/print/temp";
    public static final String EXPECTED_PDF = "./src/test/resources/pdf-project/expected/spot-91x59/page-1.pdf";


    @BeforeEach
    void setUp() {
        cleanTempDirectory(PDF_PROJECT_PATH);
    }

    @Test
    void acceptPdfPagesCompile() throws IOException, InterruptedException {
        LayoutComposer composer = createComposer(PageSize.A4, SpotSize.millimeters(91, 59));
        DecksCatalog pdfCatalog = new DecksCatalog(new File(PDF_CATALOG_PATH), ImageFormat.PDF);
        DocumentLayout pages = composer.compose(pdfCatalog);
        PdfPagesComposer pdfPagesComposer = createPdfPagesLayout();
        pdfPagesComposer.composeByLayout(pages);

        File pdfPageTemp = searchTempPdfFile("page-1.pdf");
        assertPdfFilesEquals(pdfPageTemp.getPath(), EXPECTED_PDF);
    }

    private File searchTempPdfFile(String expected) {
        File tempDir = new File(PROJECT_TEMP_PATH);
        File[] tempFiles = Objects.requireNonNull(tempDir.listFiles());
        Assertions.assertEquals(1, tempFiles.length);
        File pdfPageTemp = tempFiles[0];
        Assertions.assertEquals(expected, pdfPageTemp.getName());
        return pdfPageTemp;
    }

    private void assertPdfFilesEquals(String expectedPdf, String actualPdf) throws InterruptedException, IOException {
        CompareTool compareTool = new CompareTool();
        String result = compareTool.compareByContent(actualPdf, expectedPdf, PROJECT_TEMP_PATH);
        Assertions.assertNull(result, "Compare pdf content result: " + result);
    }

    private PdfPagesComposer createPdfPagesLayout() {
        return new PdfPagesComposerByIText(new File(PDF_PROJECT_PATH));
    }

    private LayoutComposer createComposer(PageSize pageSize, SpotSize millimeters) {
        return new LayoutComposer(pageSize, millimeters);
    }
}
