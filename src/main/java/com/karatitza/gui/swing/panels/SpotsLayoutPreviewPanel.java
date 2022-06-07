package com.karatitza.gui.swing.panels;

import com.karatitza.project.CardProject;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static javax.swing.BorderFactory.*;

public class SpotsLayoutPreviewPanel extends JPanel implements SpotsLayoutListener {
    public static final Logger LOG = LoggerFactory.getLogger(SpotsLayoutPreviewPanel.class);

    private final JLabel preview;
    private final CardProject cardProject;

    public SpotsLayoutPreviewPanel(CardProject cardProject) {
        this.cardProject = cardProject;
        this.preview = new JLabel();
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10),
                createTitledBorder("Preview of card spots disposition"))
        );
        preview.setPreferredSize(new Dimension(600, 900));
        preview.setVerticalAlignment(JLabel.CENTER);
        preview.setHorizontalAlignment(JLabel.CENTER);
        add(preview, BorderLayout.CENTER);
        refresh();
    }

    public void refresh(Integer height, Integer width, Integer space, PageFormat pageFormat) {
        SpotsPreview spotsPreview = new SpotsPreview(
                new SpotsLayout(pageFormat, SpotSize.millimeters(height, width, space)));
        ImageIcon imageIcon = buildImageFromStream(spotsPreview);
        preview.setIcon(resizeImageIcon(imageIcon));
    }

    public void refresh() {
        SpotsPreview spotsPreview = new SpotsPreview(cardProject.getSpotsLayout());
        LOG.debug("Refresh page preview: {}", spotsPreview);
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
            try (PDDocument document = PDDocument.load(spotsPreview.composeToFile(new File("./src/test/resources")))) {
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

    @Override
    public void layoutChanged() {
        refresh();
    }
}
