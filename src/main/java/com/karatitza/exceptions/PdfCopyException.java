package com.karatitza.exceptions;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;

public class PdfCopyException extends RuntimeException {

    public PdfCopyException(PdfPage page, PdfDocument document, Throwable cause) {
        super("Failed to copy PDF image %s to PDF page %s".formatted(page, document), cause);
    }
}
