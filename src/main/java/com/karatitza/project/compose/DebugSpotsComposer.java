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

import java.io.File;
import java.io.FileNotFoundException;

public class DebugSpotsComposer {

    private static final float LINE_LENGTH = 20;

    public File composeToFile(PageSize pageSize, SpotSize spotSize) {
        SpotsLayout spotsLayout = new SpotsLayout(pageSize, spotSize);
        File debugFile = createDebugFile("./src/test/resources/pdf-project/print/temp");
        try (PdfDocument debugPdf = new PdfDocument(getWriter(debugFile))) {
            PdfCanvas canvas = new PdfCanvas(debugPdf.addNewPage());
            canvas.setStrokeColor(ColorConstants.BLACK)
                    .setLineWidth(3)
                    .rectangle(canvas.getDocument().getDefaultPageSize())
                    .closePathStroke();
            for (Spot spot : spotsLayout) {
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
        return debugFile;
    }

    private PdfWriter getWriter(File debugFile) {
        try {
            return new PdfWriter(debugFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private File createDebugFile(String projectPath) {
        File debugFile = new File(projectPath + File.separator + "debug.pdf");
        debugFile.getParentFile().mkdirs();
        return debugFile;
    }

}
