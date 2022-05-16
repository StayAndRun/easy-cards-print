package com.karatitza.gui;

import com.karatitza.project.CardProject;
import com.karatitza.project.catalog.ImageFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CardCatalogControlWindow implements ActionListener {

    private final JComboBox<ImageFormat> imageFormatJComboBox;
    private final JButton selectProjectButton;
    private final JFileChooser projectChooser;
    private final JButton buildPdfButton;

    private final CardProject cardProject;

    public CardCatalogControlWindow(CardProject cardProject) {
        this.cardProject = cardProject;
        ImageFormat[] model = {ImageFormat.PDF, ImageFormat.SVG};
        this.imageFormatJComboBox = new JComboBox<>(model);
        this.buildPdfButton = new JButton("Build PDF");
        this.selectProjectButton = new JButton("Select project");
        this.projectChooser = new JFileChooser();
        this.projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.projectChooser.setCurrentDirectory(new File("/"));
    }

    public JPanel packToPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 2, 10, 10));
        controlPanel.add(selectProjectButton);
        controlPanel.add(imageFormatJComboBox);
        controlPanel.add(buildPdfButton);
        selectProjectButton.addActionListener(this);
        buildPdfButton.addActionListener(e -> cardProject.buildFinalPdf());
        return controlPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        projectChooser.showOpenDialog((Component) e.getSource());
        cardProject.selectCatalog(getSelectedProject(), getSelectedImageFormat());
    }

    private ImageFormat getSelectedImageFormat() {
        return (ImageFormat) imageFormatJComboBox.getSelectedItem();
    }

    private File getSelectedProject() {
        return projectChooser.getSelectedFile();
    }
}
