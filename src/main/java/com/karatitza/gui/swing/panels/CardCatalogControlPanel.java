package com.karatitza.gui.swing.panels;

import com.karatitza.converters.ConversionFactory;
import com.karatitza.gui.swing.worker.PdfBuildWorker;
import com.karatitza.project.CardProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.*;
import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;

public class CardCatalogControlPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(CardCatalogControlPanel.class);

    private final JButton buildPdfButton;
    private final JPanel selectConverterPanel;
    private final JProgressBar conversionProgress;

    private final CardProject cardProject;

    public CardCatalogControlPanel(CardProject cardProject) {
        this.cardProject = cardProject;
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Build pdf control"))
        );
        this.selectConverterPanel = addConverterSelectionPanel();
        this.buildPdfButton = addBuildPdfButton();
        this.conversionProgress = addConversionProgressBar();
    }

    private JProgressBar addConversionProgressBar() {
        JProgressBar conversionProgress = new JProgressBar(0, 100);
        conversionProgress.setVisible(false);
        add(conversionProgress);
        return conversionProgress;
    }

    private JButton addBuildPdfButton() {
        JButton buildPdfButton = new JButton("Build PDF");
        buildPdfButton.addActionListener(e -> preparePdfWorker().execute());
        add(buildPdfButton);
        return buildPdfButton;
    }

    private JPanel addConverterSelectionPanel() {
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
        add(selectConverterPanel);
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
}
