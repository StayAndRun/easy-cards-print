package com.karatitza.project.compose;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.karatitza.Main;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.AbstractMap;
import java.util.List;

public class PdfPagesComposerByIText extends PdfPagesComposer {
    public static final Logger LOG = LoggerFactory.getLogger(PdfPagesComposerByIText.class);

    public PdfPagesComposerByIText(File projectPath) {
        super(projectPath);
    }

    @Override
    public List<File> composeByLayout(DocumentLayout layout) {
        int pageNumber = 1;
        for (DocumentLayout.PageLayout pageLayout : layout) {
            LOG.info("Start PDF page {} compose", pageNumber);
            String pdfPageFilePath = buildTempPdfPageFileName(pageNumber);
            PdfWriter pdfWriter = createPdfWriter(pdfPageFilePath);
            try (PdfDocument mainPdfDocument = new PdfDocument(pdfWriter)) {
                mainPdfDocument.setDefaultPageSize(
                        new PageSize(pageLayout.getPageFormat().getWidth(), pageLayout.getPageFormat().getHeight()));
                placeLayoutToPdfPage(pageLayout, mainPdfDocument);
            }
            pageNumber++;
            LOG.info("PDF page successfully created: {}", pdfPageFilePath);
            pdfPageFiles.add(new File(pdfPageFilePath));
        }
        return pdfPageFiles;
    }

    private void placeLayoutToPdfPage(DocumentLayout.PageLayout pageLayout, PdfDocument mainPdfDocument) {
        PdfPageComposer pageComposer = new PdfPageComposer(mainPdfDocument.addNewPage());
        for (AbstractMap.SimpleImmutableEntry<Spot, Image> imageSpot : pageLayout) {
            Image image = imageSpot.getValue();
            Spot spot = imageSpot.getKey();
            pageComposer.placeImageToSpot(image, spot);
            LOG.info("Page: {}, placed image {}", pdfPageFiles.size() + 1, image.getName());
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

    private PdfWriter createPdfWriter(String filename) {
        try {
            return new PdfWriter(filename);
        } catch (Exception e) {
            // TODO custom exception
            throw new RuntimeException("Failed write to pdf file: " + filename);
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
