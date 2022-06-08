package com.karatitza.project.compose;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.karatitza.exceptions.ImageReadException;
import com.karatitza.exceptions.PdfCopyException;
import com.karatitza.exceptions.PdfPageWriteException;
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
        LOG.info("Page: {}, placed image {}", pdfPage.getDocument().getPageNumber(pdfPage), image);
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
        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(createImageData(imageFile));
        image.setHeight(spot.getSize().getHeight());
        image.setFixedPosition(
                spot.getCenterAlignX(spot.getSize().getWidth()),
                spot.getCenterAlignY(spot.getSize().getHeight())
        );
        document.add(image);
    }

    private PdfReader createPdfReader(String filename) {
        try {
            return new PdfReader(filename);
        } catch (Exception cause) {
            LOG.error("Failed PDF read: {}, cause {}", filename, cause.getMessage());
            throw new PdfPageWriteException(new File(filename), cause);
        }
    }

    private ImageData createImageData(File imageFile) {
        try {
            return ImageDataFactory.create(imageFile.getPath());
        } catch (MalformedURLException cause) {
            LOG.error("Failed read image file: {}, cause {}", imageFile, cause.getMessage());
            throw new ImageReadException(imageFile, cause);
        }
    }

    private PdfFormXObject pasteImageToPdfDocument(PdfDocument pdfImage) {
        try {
            return pdfImage.getFirstPage().copyAsFormXObject(pdfPage.getDocument());
        } catch (IOException cause) {
            LOG.error("Failed PDF image copy to page, cause: ", cause);
            throw new PdfCopyException(pdfPage, pdfImage, cause);
        }
    }

}
