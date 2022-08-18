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

public class PdfBuildControlPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(PdfBuildControlPanel.class);

    private final JButton buildPdfButton;
    private final JPanel selectConverterPanel;
    private final JProgressBar conversionProgress;
    private final JCheckBox disableBacksCheckBox;

    private final CardProject cardProject;

    public PdfBuildControlPanel(CardProject cardProject) {
        this.cardProject = cardProject;
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Build final PDF control"))
        );
        this.selectConverterPanel = addConverterSelectionPanel();
        this.disableBacksCheckBox = addDisableBacksCheckBox();
        this.buildPdfButton = addBuildPdfButton();
        this.conversionProgress = addConversionProgressBar();
    }

    private JProgressBar addConversionProgressBar() {
        JProgressBar conversionProgress = new JProgressBar(0, 100);
        conversionProgress.setVisible(false);
        add(conversionProgress);
        return conversionProgress;
    }

    private JCheckBox addDisableBacksCheckBox() {
        JCheckBox checkBox = new JCheckBox("Disable backs");
        checkBox.addActionListener(e -> cardProject.enableBacks(!checkBox.isSelected()));
        add(checkBox);
        return checkBox;
    }

    private JButton addBuildPdfButton() {
        JButton buildPdfButton = new JButton("Build PDF");
        buildPdfButton.addActionListener(e -> preparePdfWorker().execute());
        add(buildPdfButton);
        return buildPdfButton;
    }

    private JPanel addConverterSelectionPanel() {
        JRadioButton selectITextConverterButton = new JRadioButton("SVG -> PDF IText", true);
        selectITextConverterButton.addActionListener(e ->
                cardProject.selectConverterFactory(new ConversionFactory.ITextConversionFactory()));

        JRadioButton selectInkscapePdfConverterButton = new JRadioButton("SVG -> PDF Inkscape");
        selectInkscapePdfConverterButton.addActionListener(e ->
                cardProject.selectConverterFactory(new ConversionFactory.InkscapePdfConversionFactory()));

        JRadioButton selectInkscapePngConverterButton = new JRadioButton("SVG -> PNG Inkscape");
        selectInkscapePngConverterButton.addActionListener(e ->
                cardProject.selectConverterFactory(new ConversionFactory.InkscapePngConversionFactory()));

        ButtonGroup selectConverterButtonGroup = new ButtonGroup();
        selectConverterButtonGroup.add(selectITextConverterButton);
        selectConverterButtonGroup.add(selectInkscapePngConverterButton);
        selectConverterButtonGroup.add(selectInkscapePdfConverterButton);

        JPanel selectConverterPanel = new JPanel(new GridLayout(1, 3));
        selectConverterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        selectConverterPanel.add(new JLabel("Select SVG converter:"));
        selectConverterPanel.add(selectITextConverterButton);
        selectConverterPanel.add(selectInkscapePngConverterButton);
        selectConverterPanel.add(selectInkscapePdfConverterButton);
        add(selectConverterPanel);
        return selectConverterPanel;
    }

    private PdfBuildWorker preparePdfWorker() {
        PdfBuildWorker worker = new PdfBuildWorker(cardProject.snapshot());
        worker.addPropertyChangeListener(event -> {
            if ("progress".equals(event.getPropertyName())) {
                conversionProgress.setValue((Integer) event.getNewValue());
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
