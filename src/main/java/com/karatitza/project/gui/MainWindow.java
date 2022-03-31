package com.karatitza.project.gui;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.compose.DebugSpotsComposer;
import com.karatitza.project.layout.spots.SpotSize;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainWindow {

    public static void main(String[] args) {
        new MainWindow().open();
    }

    public void open() {
        JFrame frame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new GridLayout(1, 2));

        JLabel preview = new JLabel();
        JPanel previewPanel = new JPanel();
        previewPanel.add(preview);

        JPanel controlPanel = new ControlWindow(preview).packToPanel();

        mainPanel.add(previewPanel);
        mainPanel.add(controlPanel);

        frame.setTitle("Easy Card Print");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static class ControlWindow implements ChangeListener {

        private final JLabel preview;
        private final JSpinner spotHeight;
        private final JSpinner spotWidth;
        private final JSpinner spotSpace;

        public ControlWindow(JLabel preview) {
            this.preview = preview;
            spotHeight = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
            spotHeight.setToolTipText("test");
            spotWidth = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
            spotSpace = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        }

        public JPanel packToPanel() {
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new GridLayout(0, 1, 10, 10));
            spotHeight.addChangeListener(this);
            spotWidth.addChangeListener(this);
            spotSpace.addChangeListener(this);
            controlPanel.add(new JLabel("Spot Height"));
            controlPanel.add(spotHeight);
            controlPanel.add(new JLabel("Spot Width"));
            controlPanel.add(spotWidth);
            controlPanel.add(new JLabel("Spot Space"));
            controlPanel.add(spotSpace);
            rebuildPreviewImage();
            return controlPanel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            rebuildPreviewImage();
        }

        private void rebuildPreviewImage() {
            Integer height = Integer.parseInt(String.valueOf(spotHeight.getValue()));
            Integer width = Integer.parseInt(String.valueOf(spotWidth.getValue()));
            Integer space = Integer.parseInt(String.valueOf(spotSpace.getValue()));
            ImageIcon imageIcon = buildImage(PageSize.A4, SpotSize.millimeters(height, width, space));
            preview.setIcon(imageIcon);
        }

        public ImageIcon buildImage(PageSize pagesize, SpotSize millimeters) {
            try {
                DebugSpotsComposer spotsComposer = new DebugSpotsComposer();
                BufferedImage bufferedImage;
                try (PDDocument document = PDDocument.load(spotsComposer.composeToFile(pagesize, millimeters))) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    bufferedImage = pdfRenderer.renderImage(0);
                    document.close();
                }
                return new ImageIcon(bufferedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
