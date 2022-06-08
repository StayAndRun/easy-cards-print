package com.karatitza.exceptions;

import java.io.File;

public class PdfPageWriteException extends RuntimeException {

    public static final String MESSAGE = "Failed write PDF page to file: %s";

    public PdfPageWriteException(File file) {
        super(MESSAGE.formatted(file));
    }

    public PdfPageWriteException(File file, Throwable cause) {
        super(MESSAGE.formatted(file), cause);
    }
}
