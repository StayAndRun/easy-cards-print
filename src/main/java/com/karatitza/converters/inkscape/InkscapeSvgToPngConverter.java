package com.karatitza.converters.inkscape;

import com.karatitza.converters.TempFileProvider;
import com.karatitza.converters.inkscape.console.InkscapeShell;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InkscapeSvgToPngConverter extends AbstractImageConverter {

    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPngConverter.class);

    private final TempFileProvider tempProvider;
    private final List<Image> batchImages = new ArrayList<>();

    public InkscapeSvgToPngConverter(TempFileProvider tempProvider) {
        this.tempProvider = tempProvider;
    }

    @Override
    public ImageFormat inputFormat() {
        return ImageFormat.SVG;
    }

    @Override
    public ImageFormat outputFormat() {
        return ImageFormat.PNG;
    }

    @Override
    public Image convert(Image sourceImage) {
        throw new UnsupportedOperationException("Unsupported for PNG target format");
    }

    @Override
    public Image addToBatch(Image sourceImage) {
        batchImages.add(sourceImage);
        return tempProvider.create(sourceImage, outputFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return executeConvertBatch(batchImages);
    }

    public List<Image> executeConvertBatch(List<Image> sourceImages) {
        List<Image> convertedImages = new ArrayList<>(sourceImages.size());
        try (InkscapeShell inkscapeShell = new InkscapeShell(tempProvider.createConversionLogDir())) {
            for (Image sourceImage : sourceImages) {
                Image tempImage = tempProvider.create(sourceImage, outputFormat());
                if (sourceImage.getFormat() == inputFormat()) {
                    inkscapeShell.exportToPngFile(sourceImage.getLocation(), tempImage.getLocation());
                    fileCreationListener.accept(tempImage.getLocation());
                } else {
                    LOG.warn("Not supported input format {}, conversion skipped", sourceImage.getName());
                    copyImage(sourceImage, tempImage);
                }
                convertedImages.add(tempImage);
            }
        }
        return convertedImages;
    }

}