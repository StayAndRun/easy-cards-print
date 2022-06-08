package com.karatitza.exceptions;

public class FinalPdfBuildException extends RuntimeException {

    public FinalPdfBuildException(Exception cause) {
        super("Failed build final PDF: ", cause);
    }
}
