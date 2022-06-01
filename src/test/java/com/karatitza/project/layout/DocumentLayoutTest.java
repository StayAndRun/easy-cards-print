package com.karatitza.project.layout;

import com.itextpdf.kernel.utils.CompareTool;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.TempFilesTest;
import com.karatitza.project.compose.PdfPagesComposer;
import com.karatitza.project.compose.PdfPagesComposerByIText;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

public class DocumentLayoutTest extends TempFilesTest {

    public static final String PDF_CATALOG_PATH = "./src/test/resources/pdf-project/decks";
    public static final String PDF_PROJECT_PATH = "./src/test/resources/pdf-project";
    public static final String PROJECT_TEMP_PATH = "./src/test/resources/pdf-project/print/temp";
    public static final String EXPECTED_PDF_PAGE_1 = "./src/test/resources/expected/spot-91x59/page-1.pdf";
    public static final String EXPECTED_PDF_PAGE_2 = "./src/test/resources/expected/spot-91x59/page-2.pdf";

    @BeforeEach
    void setUp() {
        cleanupProjectTempFiles(PDF_PROJECT_PATH);
    }

    @Test
    void acceptPdfPagesCompileWithSpot91x59() throws IOException, InterruptedException {
        LayoutComposer composer = createComposer(CommonPageFormat.A4, SpotSize.millimeters(91, 59));
        DecksCatalog pdfCatalog = new DecksCatalog(new File(PDF_CATALOG_PATH));
        DocumentLayout pages = composer.compose(pdfCatalog);
        PdfPagesComposer pdfPagesComposer = createPdfPagesLayout();
        pdfPagesComposer.composeByLayout(pages);

        File tempPdfPage1 = searchTempPdfFile("page-1.pdf");
        assertPdfFilesEquals(tempPdfPage1.getPath(), EXPECTED_PDF_PAGE_1);

        File tempPdfPage2 = searchTempPdfFile("page-2.pdf");
        assertPdfFilesEquals(tempPdfPage2.getPath(), EXPECTED_PDF_PAGE_2);
    }

    @Test
    void acceptPdfPagesCompileWithSpot100x80() throws IOException, InterruptedException {
        LayoutComposer composer = createComposer(CommonPageFormat.A4, SpotSize.millimeters(100, 80));
        DecksCatalog pdfCatalog = new DecksCatalog(new File(PDF_CATALOG_PATH));
        DocumentLayout pages = composer.compose(pdfCatalog);
        PdfPagesComposer pdfPagesComposer = createPdfPagesLayout();
        pdfPagesComposer.composeByLayout(pages);
        for (int pageNumber = 1; pageNumber <= 6; pageNumber++) {
            assertPdfFilesEquals(
                    searchTempPdfFile(format("page-%d.pdf", pageNumber)).getPath(),
                    format("./src/test/resources/expected/spot-100x80/page-%d.pdf", pageNumber)
            );
        }
    }

    private File searchTempPdfFile(String expected) {
        File tempDir = new File(PROJECT_TEMP_PATH);
        File[] tempFiles = Objects.requireNonNull(tempDir.listFiles());
        Optional<File> foundFile = Arrays.stream(tempFiles).filter(file -> file.getName().equals(expected)).findFirst();
        if (!foundFile.isPresent()) {
            Assertions.fail("Not found expected file: " + expected);
        }
        return foundFile.get();
    }

    private void assertPdfFilesEquals(String expectedPdf, String actualPdf) throws InterruptedException, IOException {
        CompareTool compareTool = new CompareTool();
        String result = compareTool.compareByContent(actualPdf, expectedPdf, PROJECT_TEMP_PATH);
        Assertions.assertNull(result, "Compare pdf content result: " + result);
    }

    private PdfPagesComposer createPdfPagesLayout() {
        return new PdfPagesComposerByIText(new File(PDF_PROJECT_PATH));
    }

    private LayoutComposer createComposer(PageFormat pageFormat, SpotSize spotSize) {
        return new LayoutComposer(new DocumentLayout(new SpotsLayout(pageFormat, spotSize)));
    }
}
