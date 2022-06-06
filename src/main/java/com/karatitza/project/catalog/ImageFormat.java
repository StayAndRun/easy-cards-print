package com.karatitza.project.catalog;

public enum ImageFormat {
    SVG (".svg"),
    PDF (".pdf"),
    PNG(".png");

    private final String extension;

    ImageFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
