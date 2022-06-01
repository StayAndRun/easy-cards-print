package com.karatitza.converters.itext;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.svg.converter.SvgConverter;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempFileProvider;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ITextSvgToPdfConverter implements ImageConverter {
    public static final Logger LOG = LoggerFactory.getLogger(ITextSvgToPdfConverter.class);

    private final TempFileProvider tempFileProvider;
    private final List<Image> images = new ArrayList<>();
    private Consumer<File> fileCreationListener = defaultFileListener();

    public ITextSvgToPdfConverter(TempFileProvider tempFileProvider) {
        this.tempFileProvider = tempFileProvider;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = tempFileProvider.create(sourceImage, outputFormat());
        tryCreateFile(targetImage.getLocation());
        try {
            SvgConverter.createPdf(sourceImage.getLocation(), targetImage.getLocation());
        } catch (IOException e) {
//          TODO exception details
            throw new RuntimeException(e);
        }
        fileCreationListener.accept(targetImage.getLocation());
        return targetImage;
    }

    @Override
    public Image addToBatch(Image sourceImage) {
        if (sourceImage.getFormat() != inputFormat()) {
            LOG.warn("Not supported input format {}, conversion skipped", sourceImage.getName());
            return sourceImage;
        }
        images.add(sourceImage);
        return tempFileProvider.create(sourceImage, outputFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return images.stream().map(this::convert).toList();
    }

    @Override
    public ImageFormat inputFormat() {
        return ImageFormat.SVG;
    }

    @Override
    public ImageFormat outputFormat() {
        return ImageFormat.PDF;
    }

    @Override
    public void listenFileCreation(Consumer<File> fileCreationListener) {
        this.fileCreationListener = fileCreationListener;
    }

    private void tryCreateFile(File location) {
        if (!location.getParentFile().exists()) {
            if (!location.getParentFile().mkdirs()) {
                throw new RuntimeException("Failed path creation: " + location.getParent());
            }
        }
        try {
            PdfWriter pdfWriter = new PdfWriter(location);
            pdfWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
