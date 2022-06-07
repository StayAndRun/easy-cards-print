package com.karatitza.project.compose;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.layout.spots.Spot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

class PdfPageComposer {
    public static final Logger LOG = LoggerFactory.getLogger(PdfPageComposer.class);

    private final PdfPage pdfPage;

    public PdfPageComposer(PdfPage pdfPage) {
        this.pdfPage = pdfPage;
    }

    public void placeImageToSpot(Image image, Spot spot) {
        if (image == null) {
            return;
        }
        switch (image.getFormat()) {
            case PDF -> placePdfImage(image.getLocation(), spot);
            case PNG -> placePngImage(image.getLocation(), spot);
            default -> LOG.warn("Image format not supported for PDF placement: {}", image);
        }
    }

    private void placePdfImage(File imageFile, Spot spot) {
        try (PdfDocument pdfImage = new PdfDocument(createPdfReader(imageFile.getPath()))) {
            PdfFormXObject imageObject = pasteImageToPdfDocument(pdfImage);
            PdfCanvas canvas = new PdfCanvas(pdfPage);
            canvas.addXObjectAt(
                    imageObject,
                    spot.getCenterAlignX(imageObject.getWidth()),
                    spot.getCenterAlignY(imageObject.getHeight())
            );
            canvas.release();
        }
    }

    private void placePngImage(File imageFile, Spot spot) {
        Document document = new Document(pdfPage.getDocument());
        com.itextpdf.layout.element.Image pngImage = new com.itextpdf.layout.element.Image(createPngImageData(imageFile));
        pngImage.setHeight(spot.getSize().getHeight());
        pngImage.setFixedPosition(
                spot.getCenterAlignX(spot.getSize().getWidth()),
                spot.getCenterAlignY(spot.getSize().getHeight())
        );
        document.add(pngImage);
    }

    private PdfReader createPdfReader(String filename) {
        try {
            return new PdfReader(filename);
        } catch (Exception e) {
            // TODO custom exception
            throw new RuntimeException("Failed to read pdf file: " + filename);
        }
    }

    private ImageData createPngImageData(File imageFile) {
        try {
            return ImageDataFactory.create(imageFile.getPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfFormXObject pasteImageToPdfDocument(PdfDocument pdfImage) {
        try {
            return pdfImage.getFirstPage().copyAsFormXObject(pdfPage.getDocument());
        } catch (IOException e) {
            // TODO custom exception
            throw new RuntimeException(e);
        }
    }

}
