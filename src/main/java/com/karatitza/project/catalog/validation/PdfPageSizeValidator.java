package com.karatitza.project.catalog.validation;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.IOException;

public class PdfPageSizeValidator implements FileValidator {

    private final Rectangle size;

    public PdfPageSizeValidator(Rectangle size) {
        this.size = size;
    }

    @Override
    public boolean isValid(File file) {
        try (PdfReader pdfReader = new PdfReader(file.getAbsolutePath())) {
            PdfDocument pdfDocument = new PdfDocument(pdfReader);
            PdfPage firstPage = pdfDocument.getFirstPage();
            if (size.equals(firstPage.getPageSize())) {
                throw new UnsupportedOperationException("Unsupport another size: " + firstPage.getPageSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
