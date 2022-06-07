package com.karatitza.project.compose;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.karatitza.project.MeasureUtils;
import com.karatitza.project.PathConstants;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PdfDocumentComposer {
    public static final Logger LOG = LoggerFactory.getLogger(DocumentLayout.class);

    private final PdfPagesComposer pagesComposer;
    private final File projectPath;

    public PdfDocumentComposer(PdfPagesComposer pagesComposer, File projectPath) {
        this.pagesComposer = pagesComposer;
        this.projectPath = projectPath;
    }

    public PdfDocumentComposer(File projectPath) {
        this.pagesComposer = new PdfPagesComposerByIText(projectPath);
        this.projectPath = projectPath;
    }

    public File compose(DocumentLayout documentLayout) {
        List<File> pages = pagesComposer.composeByLayout(documentLayout);
        File finalPdfFile = buildFinalPdfFile(documentLayout.getSpots());
        LOG.info("Start merge pages to final PDF file: {}", finalPdfFile);
        try (PdfDocument finalPdfDocument = createFinalPdfDocument(finalPdfFile)) {
            PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);
            for (File page : pages) {
                try (PdfDocument pdfPageDocument = readPageDocument(page)) {
                    pdfMerger.merge(pdfPageDocument, 1, 1);
                    LOG.debug("Merged page: {}", page);
                }
            }
            pdfMerger.close();
        }
        LOG.info("Final PDF file successfully built: {}", finalPdfFile);
        return finalPdfFile;
    }

    private PdfDocument readPageDocument(File page) {
        try {
            return new PdfDocument(new PdfReader(page));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfDocument createFinalPdfDocument(File file) {
        try {
            return new PdfDocument(new PdfWriter(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private File buildFinalPdfFile(SpotsLayout spots) {
        String documentName = buildFileName(spots);
        return new File(projectPath.getPath() + PathConstants.PROJECT_PRINT_PATH, documentName);
    }

    private String buildFileName(SpotsLayout spots) {
        try {
            SpotSize spotSize = spots.getSpotSize();
            return "%s(%s-%sx%s-%s)%s".formatted(
                    projectPath.getName(),
                    spots.getPageFormat(),
                    MeasureUtils.pointsToMillimetersRound(spotSize.getHeight()),
                    MeasureUtils.pointsToMillimetersRound(spotSize.getWidth()),
                    MeasureUtils.pointsToMillimetersRound(spotSize.getSpace()),
                    ImageFormat.PDF.getExtension()
            );
        } catch (Exception exception) {
            return "%s-%s%s".formatted(
                    projectPath.getName(),
                    UUID.randomUUID().toString(),
                    ImageFormat.PDF.getExtension()
            );
        }
    }
}
