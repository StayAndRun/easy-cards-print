package com.karatitza.gui.swing.areas;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.gui.swing.worker.PdfBuildWorker;
import com.karatitza.project.CardProject;
import com.karatitza.project.catalog.DecksCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static javax.swing.BorderFactory.*;
import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;

public class CardCatalogControlArea implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(CardCatalogControlArea.class);

    private final JButton selectProjectButton;
    private final JFileChooser projectChooser;
    private final JButton buildPdfButton;
    private final JPanel selectConverterPanel;
    private final CatalogPreviewArea previewArea;
    private final JProgressBar conversionProgress;

    private final CardProject cardProject;

    public CardCatalogControlArea(CardProject cardProject) {
        this.cardProject = cardProject;
        this.buildPdfButton = new JButton("Build PDF");
        this.selectProjectButton = new JButton("Select project");
        this.projectChooser = new JFileChooser();
        this.projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        trySetLatestProjectRoot(cardProject);
        this.selectConverterPanel = buildConverterSelectionPanel();
        this.previewArea = new CatalogPreviewArea();
        this.conversionProgress = new JProgressBar(0, 100);
        this.conversionProgress.setVisible(false);
    }

    public JPanel packToPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2, 10, 10));
        controlPanel.setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Project catalog control"))
        );
        controlPanel.add(selectProjectButton);
        controlPanel.add(previewArea.packToPanel());
        controlPanel.add(new JLabel());
        controlPanel.add(buildPdfButton);
        controlPanel.add(selectConverterPanel);
        selectProjectButton.addActionListener(this);
        buildPdfButton.addActionListener(e -> preparePdfWorker().execute());
        controlPanel.add(conversionProgress);
        return controlPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedOption = projectChooser.showOpenDialog((Component) e.getSource());
        if (selectedOption == JFileChooser.APPROVE_OPTION) {
            DecksCatalog catalog = cardProject.selectCatalog(getSelectedProject());
            previewArea.refresh(catalog);
        }
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

    private PdfBuildWorker preparePdfWorker() {
        PdfBuildWorker worker = new PdfBuildWorker(cardProject.snapshot());
        worker.addPropertyChangeListener(event -> {
            if ("progress".equals(event.getPropertyName())) {
                conversionProgress.setValue((Integer) event.getNewValue());
                LOG.info("Incoming progress: {}", event.getNewValue());
            }
        });
        worker.addPropertyChangeListener(event -> {
            if ("state".equals(event.getPropertyName()) && (DONE.equals(event.getNewValue()))) {
                buildPdfButton.setEnabled(true);
            }
        });
        worker.addPropertyChangeListener(event -> {
            if ("state".equals(event.getPropertyName()) && (STARTED.equals(event.getNewValue()))) {
                buildPdfButton.setEnabled(false);
            }
        });
        conversionProgress.setVisible(true);
        return worker;
    }

    private File getSelectedProject() {
        return projectChooser.getSelectedFile();
    }
}
