package com.karatitza.project;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.karatitza.Main;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotSize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

public class PdfPagesComposerByIText extends PdfPagesComposer {

    public PdfPagesComposerByIText(File projectPath) {
        super(projectPath);
    }

    @Override
    public List<File> composeByLayout(DocumentLayout layout) {
        int pageNumber = 1;
        for (DocumentLayout.PageLayout pageLayout : layout) {
            String pdfPageFilePath = buildTempPdfPageFileName(pageNumber);
            PdfWriter pdfWriter = createPdfWriter(pdfPageFilePath);
            try (PdfDocument pdfDocument = new PdfDocument(pdfWriter)) {
                pdfDocument.setDefaultPageSize(pageLayout.getPageSize());
                placeLayoutToPdfDocument(pageLayout, pdfDocument);
            }
            pageNumber++;
            pdfPageFiles.add(new File(pdfPageFilePath));
        }
        return pdfPageFiles;
    }

    private void placeLayoutToPdfDocument(DocumentLayout.PageLayout pageLayout, PdfDocument pdfDocument) {
        PdfPage newPdfPage = pdfDocument.addNewPage();
        for (AbstractMap.SimpleImmutableEntry<Spot, Image> imageSpot : pageLayout) {
            Image image = imageSpot.getValue();
            Spot spot = imageSpot.getKey();
            if (image == null) {
                continue;
            }
            try (PdfDocument pdfImage = new PdfDocument(createPdfReader(image.getLocation().getPath()))) {
                PdfFormXObject imageObject = pasteImageToPdfDocument(pdfDocument, pdfImage);
                PdfCanvas canvas = new PdfCanvas(newPdfPage).addXObjectAt(
                        imageObject,
                        spot.getCenterAlignX(imageObject.getWidth()),
                        spot.getCenterAlignY(imageObject.getHeight())
                );
                if (isDrawDebugMesh) {
                    drawDebugMesh(canvas, pageLayout.getSpotSize(), spot);
                }
                canvas.release();
            }
        }
    }

    private String buildTempPdfPageFileName(int pageNumber) {
        String tempDirPath = projectPath + Main.TEMP_FILES_RELATE_PATH;
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            if (!tempDir.mkdirs()) {
                throw new RuntimeException("Failed temp dir creation: " + tempDir);
            }
        }
        return String.format("%s%spage-%d.pdf", tempDirPath, File.separator, pageNumber);
    }

    private PdfFormXObject pasteImageToPdfDocument(PdfDocument pdfDocument, PdfDocument pdfImage) {
        try {
            return pdfImage.getFirstPage().copyAsFormXObject(pdfDocument);
        } catch (IOException e) {
            // TODO custom exception
            throw new RuntimeException(e);
        }
    }

    private PdfReader createPdfReader(String filename) {
        try {
            return new PdfReader(filename);
        } catch (IOException e) {
            // TODO custom exception
            throw new RuntimeException(e);
        }
    }

    private PdfWriter createPdfWriter(String filename) {
        try {
            return new PdfWriter(filename);
        } catch (FileNotFoundException e) {
            // TODO custom exception
            throw new RuntimeException(e);
        }
    }

    private void drawDebugMesh(PdfCanvas canvas, SpotSize spotSize, Spot spot) {
        Rectangle pageBorder = canvas.getDocument().getDefaultPageSize();
        Rectangle spotRectangle = new Rectangle(
                spot.getCenterAlignX(spotSize.getWidth()),
                spot.getCenterAlignY(spotSize.getHeight()),
                spotSize.getWidth(), spotSize.getHeight()
        );
        canvas.setStrokeColor(ColorConstants.RED)
                .rectangle(pageBorder)
                .closePathStroke();
        canvas.setStrokeColor(ColorConstants.GREEN)
                .rectangle(spotRectangle)
                .closePathStroke();
        canvas.setStrokeColor(ColorConstants.BLUE)
                .moveTo(spot.getX(), 0)
                .lineTo(spot.getX(), pageBorder.getHeight())
                .moveTo(0, spot.getY())
                .lineTo(pageBorder.getWidth(), spot.getY())
                .closePathStroke();
    }
}
