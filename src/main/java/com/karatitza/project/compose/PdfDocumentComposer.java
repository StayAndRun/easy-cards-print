package com.karatitza.project.compose;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.karatitza.project.PathConstants;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.layout.DocumentLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PdfDocumentComposer {

    private final PdfPagesComposer pagesComposer;
    private final File projectPath;

    public PdfDocumentComposer(PdfPagesComposer pagesComposer, File projectPath) {
        this.pagesComposer = pagesComposer;
        this.projectPath = projectPath;
    }

    public PdfDocumentComposer(File projectPath) {
        this.pagesComposer = new PdfPagesComposerByIText(projectPath);
        this.projectPath = projectPath;
    }

    public File compose(DocumentLayout documentLayout) {
        List<File> pages = pagesComposer.composeByLayout(documentLayout);
        File finalPdfFile = buildFinalPdfFileName();
        try (PdfDocument finalPdfDocument = createFinalPdfDocument(finalPdfFile)) {
            PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);
            for (File page : pages) {
                try (PdfDocument pdfPageDocument = readPageDocument(page)) {
                    pdfMerger.merge(pdfPageDocument, 1, 1);
                }
            }
            pdfMerger.close();
        }
        return finalPdfFile;
    }

    private PdfDocument readPageDocument(File page) {
        try {
            return new PdfDocument(new PdfReader(page));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfDocument createFinalPdfDocument(File file) {
        try {
            return new PdfDocument(new PdfWriter(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private File buildFinalPdfFileName() {
        String projectName = projectPath.getName();
        return new File(
                projectPath.getPath() + PathConstants.PROJECT_PRINT_PATH
                        + File.separator + projectName + ImageFormat.PDF.getExtension()
        );
    }
}
