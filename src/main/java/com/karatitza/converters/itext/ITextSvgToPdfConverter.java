package com.karatitza.converters.itext;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.svg.converter.SvgConverter;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ITextSvgToPdfConverter implements ImageConverter {

    private final TempImageFactory tempImageFactory;
    private final List<Image> images = new ArrayList<>();
    private Consumer<File> fileCreationListener = defaultFileListener();

    public ITextSvgToPdfConverter(TempImageFactory tempImageFactory) {
        this.tempImageFactory = tempImageFactory;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = tempImageFactory.create(sourceImage, fileFormat());
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
        images.add(sourceImage);
        return tempImageFactory.create(sourceImage, fileFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return images.stream().map(this::convert).toList();
    }

    @Override
    public ImageFormat fileFormat() {
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
