package com.karatitza.exceptions;

import java.io.File;

public class ImageReadException extends RuntimeException {

    public static final String MESSAGE = "Failed to read image data from file: %s";

    public ImageReadException(File imageFile, Throwable cause) {
        super(MESSAGE.formatted(imageFile), cause);
    }

    public ImageReadException(File imageFile) {
        super(MESSAGE.formatted(imageFile));
    }
}
