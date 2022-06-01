package com.karatitza.converters;

import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public interface ImageConverter {

    Image convert(Image sourceImage);

    Image addToBatch(Image sourceImage);

    List<Image> convertBatch();

    ImageFormat outputFormat();

    ImageFormat inputFormat();

    void listenFileCreation(Consumer<File> fileCreationListener);

    default Consumer<File> defaultFileListener() {
        return file -> InkscapeSvgToPdfConverter.LOG.info("Accepted file conversion: {}", file);
    }
}
