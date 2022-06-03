package com.karatitza.project.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

public class Image {
    private final static Logger LOG = LoggerFactory.getLogger(Image.class);

    private final String name;
    private final File location;
    private final File deckLocation;
    private final ImageFormat format;

    public Image(File location, File deckLocation) throws FileNotFoundException {
        this.name = location.getName();
        this.location = location;
        this.deckLocation = deckLocation;
        this.format = extractImageFormat(location);
    }

    public static Optional<Image> fromFile(File imageFile, File deckLocation) {
        try {
            return Optional.of(new Image(imageFile, deckLocation));
        } catch (FileNotFoundException e) {
            LOG.warn("Found not supported image format: {}", imageFile.getName());
            return Optional.empty();
        }
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

    private static ImageFormat extractImageFormat(File location) throws FileNotFoundException {
        String fileName = location.getName();
        for (ImageFormat format : ImageFormat.values()) {
            if (fileName.endsWith(format.getExtension())) {
                return format;
            }
        }
        throw new FileNotFoundException("Not found supported image format for file: " + location);
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + "', " +
                "deck='" + deckLocation.getName() + '\'' +
                '}';
    }
}
