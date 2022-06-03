package com.karatitza.converters.inkscape;

import com.karatitza.converters.ImageConverter;
import com.karatitza.project.catalog.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.function.Consumer;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public abstract class AbstractImageConverter implements ImageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(ImageConverter.class);

    protected Consumer<File> fileCreationListener = defaultFileListener();

    protected void copyImage(Image sourceImage, Image targetImage) {
        File sourceImageLocation = sourceImage.getLocation();
        File targetImageLocation = targetImage.getLocation();
        try {
            if (!targetImageLocation.exists()) {
                targetImageLocation.mkdirs();
                targetImageLocation.createNewFile();
            };
            Files.copy(
                    sourceImageLocation.toPath(), targetImageLocation.toPath(),
                    COPY_ATTRIBUTES, REPLACE_EXISTING);
        } catch (IOException exception) {
            LOG.error(MessageFormat.format("Failed to copy original file: {0} to location: {1}, Cause: ",
                    sourceImageLocation.getPath(), targetImageLocation.toPath()), exception);
        }
    }

    public void listenFileCreation(Consumer<File> fileCreationListener) {
        this.fileCreationListener = fileCreationListener;
    }
}
