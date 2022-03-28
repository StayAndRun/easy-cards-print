package com.karatitza.project.catalog;

import java.io.File;

public class Image {

    private final String name;
    private final File location;
    private final File deckLocation;
    private final ImageFormat format;

    public Image(File location, File deckLocation, ImageFormat format) {
        this.name = location.getName();
        this.location = location;
        this.deckLocation = deckLocation;
        this.format = format;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public File getLocation() {
        return location;
    }

    public File getDeckLocation() {
        return deckLocation;
    }
}
