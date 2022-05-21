package com.karatitza.gui.swing.areas;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.project.CardProject;
import com.karatitza.project.catalog.ImageFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CardCatalogControlArea implements ActionListener {

    private final JComboBox<ImageFormat> imageFormatJComboBox;
    private final JButton selectProjectButton;
    private final JFileChooser projectChooser;
    private final JButton buildPdfButton;
    private final JPanel selectConverterPanel;

    private final CardProject cardProject;

    public CardCatalogControlArea(CardProject cardProject) {
        this.cardProject = cardProject;
        ImageFormat[] model = {ImageFormat.PDF, ImageFormat.SVG};
        this.imageFormatJComboBox = new JComboBox<>(model);
        this.buildPdfButton = new JButton("Build PDF");
        this.selectProjectButton = new JButton("Select project");
        this.projectChooser = new JFileChooser();
        this.projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.projectChooser.setCurrentDirectory(new File("."));
        this.selectConverterPanel = buildConverterSelectionPanel();
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
        controlPanel.setLayout(new GridLayout(2, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        controlPanel.add(selectProjectButton);
        controlPanel.add(imageFormatJComboBox);
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
            cardProject.selectCatalog(getSelectedProject(), getSelectedImageFormat());
        }
    }

    private ImageFormat getSelectedImageFormat() {
        return (ImageFormat) imageFormatJComboBox.getSelectedItem();
    }

    private File getSelectedProject() {
        return projectChooser.getSelectedFile();
    }
}
