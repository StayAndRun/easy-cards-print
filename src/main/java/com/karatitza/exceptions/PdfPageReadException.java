package com.karatitza.exceptions;

import java.io.File;

public class PdfPageReadException extends RuntimeException {

    public static final String MESSAGE = "Failed read PDF from file: %s";

    public PdfPageReadException(File file) {
        super(MESSAGE.formatted(file));
    }

    public PdfPageReadException(File file, Throwable cause) {
        super(MESSAGE.formatted(file), cause);
    }
}
