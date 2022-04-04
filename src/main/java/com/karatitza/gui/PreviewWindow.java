package com.karatitza.gui;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.compose.DebugSpotsComposer;
import com.karatitza.project.layout.spots.SpotSize;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
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
        preview.setPreferredSize(new Dimension(600, 900));
        preview.setVerticalAlignment(JLabel.CENTER);
        preview.setHorizontalAlignment(JLabel.CENTER);
        previewPanel.add(preview);
        return previewPanel;
    }

    public void refresh(Integer height, Integer width, Integer space, PageSize pageSize) {
        ImageIcon imageIcon = buildImageFromStream(pageSize, SpotSize.millimeters(height, width, space));
        System.out.println("Image size: " + imageIcon.getIconWidth() + " " + imageIcon.getIconHeight());
        preview.setIcon(resizeImageIcon(imageIcon));
    }

    private ImageIcon resizeImageIcon(ImageIcon imageIcon) {
        System.out.println("Previes size: " + preview.getSize());
        float scaleFactor = (float) (imageIcon.getIconWidth() / preview.getPreferredSize().getWidth());
        System.out.println("Scale factor: " + scaleFactor);
        if (scaleFactor < 1) {
            return imageIcon;
        }
        int scaledHeight = Math.round(imageIcon.getIconHeight() / scaleFactor);
        int scaledWidth = Math.round(imageIcon.getIconWidth() / scaleFactor);
        return new ImageIcon(
                imageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)
        );
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
