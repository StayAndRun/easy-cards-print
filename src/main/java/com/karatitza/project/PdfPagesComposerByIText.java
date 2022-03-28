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
import com.karatitza.project.layout.cards.CardsLayout;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfPagesComposerByIText extends PdfPagesComposer {

    public PdfPagesComposerByIText(File projectPath) {
        super(projectPath);
    }

    @Override
    public void composeByLayout(DocumentLayout layout) {
        SpotsLayout spots = layout.getSpots();
        int pageNumber = 1;
        for (CardsLayout cardsLayout : layout) {
            PdfWriter pdfWriter = createPdfWriter(buildTempPdfPageFileName(pageNumber));
            try (PdfDocument pdfDocument = new PdfDocument(pdfWriter)) {
                pdfDocument.setDefaultPageSize(spots.getPageSize());
                PdfPage pdfFrontPage = pdfDocument.addNewPage();
                PdfPage pdfBackPage = pdfDocument.addNewPage();
                for (SpotsLayout.LayoutIterator iterator = spots.layoutIterator(); iterator.hasNext(); ) {
                    Image frontImage = cardsLayout.getFrontLayout().getImage(
                            iterator.getColumnCursor(), iterator.getRowCursor()
                    );
                    Image backImage = cardsLayout.getBackLayout().getImage(
                            iterator.getColumnCursor(), iterator.getRowCursor()
                    );
                    Spot spot = iterator.next();
                    if (frontImage == null) {
                        continue;
                    }
                    try (PdfDocument frontImagePdf = new PdfDocument(createPdfReader(frontImage.getLocation().getAbsolutePath()))) {
                        PdfFormXObject frontImageObject = frontImagePdf.getFirstPage().copyAsFormXObject(pdfDocument);
                        new PdfCanvas(pdfFrontPage).addXObjectAt(
                                frontImageObject,
                                spot.getX() - frontImageObject.getWidth() / 2,
                                spot.getY() - frontImageObject.getHeight() / 2
                        );
                    } catch (IOException e) {
//                      TODO custom exception
                        throw new RuntimeException(e);
                    }
                    if (backImage == null) {
                        continue;
                    }
                    try (PdfDocument backImagePdf = new PdfDocument(createPdfReader(backImage.getLocation().getAbsolutePath()))) {
                        PdfFormXObject backImageObject = backImagePdf.getFirstPage().copyAsFormXObject(pdfDocument);
                        new PdfCanvas(pdfBackPage).addXObjectAt(
                                backImageObject,
                                spot.getX() - backImageObject.getWidth() / 2,
                                spot.getY() - backImageObject.getHeight() / 2
                        );
                    } catch (IOException e) {
//                      TODO custom exception
                        throw new RuntimeException(e);
                    }
                }
            }
            pageNumber++;
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

    private void drawBorder(float pageSizeWidth, float pageSizeHeight, PdfCanvas canvas) {
        Rectangle rectangle = new Rectangle(0, 0, pageSizeWidth, pageSizeHeight);
        canvas.setStrokeColor(ColorConstants.BLACK)
                .rectangle(rectangle)
                .stroke();
    }
}
