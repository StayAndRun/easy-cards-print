package com.karatitza.gui.swing.areas;

import com.karatitza.project.CardProject;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javax.swing.BorderFactory.*;

public class SpotsLayoutPreviewArea {

    private final JLabel preview;
    private final CardProject cardProject;

    public SpotsLayoutPreviewArea(CardProject cardProject) {
        this.cardProject = cardProject;
        this.preview = new JLabel();
    }

    public JPanel packToPanel() {
        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10),
                createTitledBorder("Preview of card spots disposition"))
        );
        preview.setPreferredSize(new Dimension(600, 900));
        preview.setVerticalAlignment(JLabel.CENTER);
        preview.setHorizontalAlignment(JLabel.CENTER);
        previewPanel.add(preview);
        return previewPanel;
    }

    public void refresh(Integer height, Integer width, Integer space, PageFormat pageFormat) {
        SpotsPreview spotsPreview = cardProject.defineSpots(pageFormat, SpotSize.millimeters(height, width, space));
        ImageIcon imageIcon = buildImageFromStream(spotsPreview);
        preview.setIcon(resizeImageIcon(imageIcon));
    }

    private ImageIcon resizeImageIcon(ImageIcon imageIcon) {
        // TODO Refactor danger numeric cast
        float scaleFactor = (float) (imageIcon.getIconWidth() / preview.getPreferredSize().getWidth());
        if (scaleFactor < 1) {
            return imageIcon;
        }
        int scaledHeight = Math.round(imageIcon.getIconHeight() / scaleFactor);
        int scaledWidth = Math.round(imageIcon.getIconWidth() / scaleFactor);
        return new ImageIcon(
                imageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)
        );
    }

    private ImageIcon buildImageFromFile(SpotsPreview spotsPreview) {
        try {
            BufferedImage bufferedImage;
            try (PDDocument document = PDDocument.load(spotsPreview.composeToFile())) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                bufferedImage = pdfRenderer.renderImage(0);
            }
            return new ImageIcon(bufferedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageIcon buildImageFromStream(SpotsPreview spotsPreview) {
        try (ByteArrayInputStream byteArrayInputStream = spotsPreview.composeToStream()) {
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
