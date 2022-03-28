package com.karatitza.converters.itext;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.svg.converter.SvgConverter;
import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;

import java.io.File;
import java.io.IOException;

public class ITextSvgToPdfConverter implements ImageConverter {

    private final TempImageFactory tempImageFactory;

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
        return targetImage;
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

    @Override
    public ImageFormat fileFormat() {
        return ImageFormat.PDF;
    }
}
