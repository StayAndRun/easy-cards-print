package com.karatitza.project.compose;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.io.*;

public class SpotsPreview {

    private static final float LINE_LENGTH = 20;

    private final SpotsLayout spotsLayout;

    public SpotsPreview(SpotsLayout spotsLayout) {
        this.spotsLayout = spotsLayout;
    }

    public File composeToFile(File directory) {
        File debugFile = createDebugFile(directory);
        createPdfDocument(getPdfWriterToFile(debugFile));
        return debugFile;
    }

    public ByteArrayInputStream composeToStream() {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            createPdfDocument(new PdfWriter(outStream));
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createPdfDocument(PdfWriter writer) {
        try (PdfDocument debugPdf = new PdfDocument(writer)) {
            debugPdf.setDefaultPageSize(
                    new PageSize(spotsLayout.getPageFormat().getWidth(), spotsLayout.getPageFormat().getHeight()));
            PdfCanvas canvas = new PdfCanvas(debugPdf.addNewPage());
            canvas.setStrokeColor(ColorConstants.BLACK)
                    .setLineWidth(3)
                    .rectangle(canvas.getDocument().getDefaultPageSize())
                    .closePathStroke();
            for (Spot spot : this.spotsLayout) {
                SpotSize spotSize = spotsLayout.getSpotSize();
                Rectangle spotRectangle = new Rectangle(
                        spot.getCenterAlignX(spotSize.getWidth()),
                        spot.getCenterAlignY(spotSize.getHeight()),
                        spotSize.getWidth(), spotSize.getHeight()
                );
                canvas.setStrokeColor(ColorConstants.GREEN)
                        .setLineWidth(4)
                        .rectangle(spotRectangle)
                        .closePathStroke();
                canvas.setStrokeColor(ColorConstants.BLUE)
                        .setLineWidth(2)
                        .moveTo(spot.getX(), spot.getY() - LINE_LENGTH)
                        .lineTo(spot.getX(), spot.getY() + LINE_LENGTH)
                        .moveTo(spot.getX() - LINE_LENGTH, spot.getY())
                        .lineTo(spot.getX() + LINE_LENGTH, spot.getY())
                        .closePathStroke();
            }
        }
    }

    private PdfWriter getPdfWriterToFile(File debugFile) {
        try {
            return new PdfWriter(debugFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private File createDebugFile(File dir) {
        File debugFile = new File(dir, "debug.pdf");
        debugFile.getParentFile().mkdirs();
        return debugFile;
    }

    @Override
    public String toString() {
        return "SpotsPreview{" +
                "spotsLayout=" + spotsLayout +
                '}';
    }
}
