package com.karatitza.project.gui;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.compose.DebugSpotsComposer;
import com.karatitza.project.layout.spots.SpotSize;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PreviewWindow {

    private final JLabel preview;

    public PreviewWindow() {
        this.preview = new JLabel();
    }

    public JPanel packToPanel() {
        JPanel previewPanel = new JPanel();
        previewPanel.add(preview);
        return previewPanel;
    }

    public void refresh(Integer height, Integer width, Integer space) {
        ImageIcon imageIcon = buildImageFromStream(PageSize.A4, SpotSize.millimeters(height, width, space));
        preview.setIcon(imageIcon);
    }

    private ImageIcon buildImageFromFile(PageSize pagesize, SpotSize millimeters) {
        try {
            DebugSpotsComposer spotsComposer = new DebugSpotsComposer();
            BufferedImage bufferedImage;
            try (PDDocument document = PDDocument.load(spotsComposer.composeToFile(pagesize, millimeters))) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                bufferedImage = pdfRenderer.renderImage(0);
            }
            return new ImageIcon(bufferedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageIcon buildImageFromStream(PageSize pagesize, SpotSize spotSize) {
        DebugSpotsComposer spotsComposer = new DebugSpotsComposer();
        try (ByteArrayInputStream byteArrayInputStream = spotsComposer.composeToStream(pagesize, spotSize)) {
            try (PDDocument document = PDDocument.load(byteArrayInputStream)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                return new ImageIcon(pdfRenderer.renderImage(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
