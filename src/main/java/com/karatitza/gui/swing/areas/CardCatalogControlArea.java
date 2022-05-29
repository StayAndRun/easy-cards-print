package com.karatitza.gui.swing.areas;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.project.CardProject;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static javax.swing.BorderFactory.*;

public class CardCatalogControlArea implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(CardCatalogControlArea.class);

    private final JComboBox<ImageFormat> imageFormatJComboBox;
    private final JButton selectProjectButton;
    private final JFileChooser projectChooser;
    private final JButton buildPdfButton;
    private final JPanel selectConverterPanel;
    private final CatalogPreviewArea previewArea;

    private final CardProject cardProject;

    public CardCatalogControlArea(CardProject cardProject) {
        this.cardProject = cardProject;
        ImageFormat[] model = {ImageFormat.PDF, ImageFormat.SVG};
        this.imageFormatJComboBox = new JComboBox<>(model);
        this.buildPdfButton = new JButton("Build PDF");
        this.selectProjectButton = new JButton("Select project");
        this.projectChooser = new JFileChooser();
        this.projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        trySetLatestProjectRoot(cardProject);
        this.selectConverterPanel = buildConverterSelectionPanel();
        this.previewArea = new CatalogPreviewArea();
    }

    private void trySetLatestProjectRoot(CardProject cardProject) {
        try {
            this.projectChooser.setCurrentDirectory(cardProject.getProjectRoot());
        } catch (IndexOutOfBoundsException exception) {
            LOG.warn("Failed to set latest project root: ", exception);
        }
    }

    private JPanel buildConverterSelectionPanel() {
        JRadioButton selectITextConverterButton = new JRadioButton("IText", true);
        selectITextConverterButton.addActionListener(e ->
                cardProject.selectConverterFactory(new ConversionFactory.ITextConversionFactory()));
        JRadioButton selectInkscapeConverterButton = new JRadioButton("Inkscape");
        selectInkscapeConverterButton.addActionListener(e ->
                cardProject.selectConverterFactory(new ConversionFactory.InkscapeConversionFactory()));
        ButtonGroup selectConverterButtonGroup = new ButtonGroup();
        selectConverterButtonGroup.add(selectITextConverterButton);
        selectConverterButtonGroup.add(selectInkscapeConverterButton);
        JPanel selectConverterPanel = new JPanel(new GridLayout(0, 1));
        selectConverterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        selectConverterPanel.add(new JLabel("Select SVG converter:"));
        selectConverterPanel.add(selectITextConverterButton);
        selectConverterPanel.add(selectInkscapeConverterButton);
        return selectConverterPanel;
    }

    public JPanel packToPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2, 10, 10));
        controlPanel.setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Project catalog control"))
        );
        controlPanel.add(selectProjectButton);
        controlPanel.add(imageFormatJComboBox);
        controlPanel.add(previewArea.packToPanel());
        controlPanel.add(new JLabel());
        controlPanel.add(buildPdfButton);
        controlPanel.add(selectConverterPanel);
        selectProjectButton.addActionListener(this);
        buildPdfButton.addActionListener(e -> cardProject.buildFinalPdf());
        return controlPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedOption = projectChooser.showOpenDialog((Component) e.getSource());
        if (selectedOption == JFileChooser.APPROVE_OPTION) {
            DecksCatalog catalog = cardProject.selectCatalog(getSelectedProject(), getSelectedImageFormat());
            previewArea.refresh(catalog);
        }
    }

    private ImageFormat getSelectedImageFormat() {
        return (ImageFormat) imageFormatJComboBox.getSelectedItem();
    }

    private File getSelectedProject() {
        return projectChooser.getSelectedFile();
    }
}
