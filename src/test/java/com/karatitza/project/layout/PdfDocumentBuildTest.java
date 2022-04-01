package com.karatitza.project.layout;

import com.karatitza.project.PdfDocumentComposer;
import com.karatitza.project.compose.PdfPagesComposer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PdfDocumentBuildTest {

    public static final String PROJECT_TEMP_PATH = "./src/test/resources/pdf-doc-build/print/temp";
    public static final String PROJECT_PATH = "./src/test/resources/pdf-doc-build";

    @Test
    void acceptFinalDocCreatedFromPages() {
        DocumentLayout documentLayout = mock(DocumentLayout.class);
        PdfPagesComposer pageComposer = mock(PdfPagesComposer.class);
        List<File> testFiles = Arrays.asList(Objects.requireNonNull(new File(PROJECT_TEMP_PATH).listFiles()));
        when(pageComposer.composeByLayout(documentLayout)).thenReturn(testFiles);

        PdfDocumentComposer composer = new PdfDocumentComposer(pageComposer, new File(PROJECT_PATH));
        File resultFile = composer.compose(documentLayout);

        Assertions.assertTrue(resultFile.exists());
        Assertions.assertEquals("pdf-doc-build.pdf", resultFile.getName());
    }
}

