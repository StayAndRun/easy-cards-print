package com.karatitza.gui;

import com.itextpdf.kernel.geom.PageSize;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SpotControlWindow implements ChangeListener, ItemListener {

    private final PreviewWindow previewWindow;
    private final JSpinner spotHeight;
    private final JSpinner spotWidth;
    private final JSpinner spotSpace;
    private final JComboBox<PageSize> pageSizeJComboBox;

    public SpotControlWindow(PreviewWindow previewWindow) {
        this.previewWindow = previewWindow;
        spotHeight = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
        spotHeight.setToolTipText("test");
        spotWidth = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
        spotSpace = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        ComboBoxModel<PageSize> comboBoxModel = new DefaultComboBoxModel<>(
                new PageSize[]{PageSize.A4, PageSize.A3, PageSize.A2}
        );
        comboBoxModel.setSelectedItem(PageSize.A4);
        pageSizeJComboBox = new JComboBox<>(comboBoxModel);
    }

    public JPanel packToPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 2, 10, 10));
        spotHeight.addChangeListener(this);
        spotWidth.addChangeListener(this);
        spotSpace.addChangeListener(this);
        pageSizeJComboBox.addItemListener(this);
        controlPanel.add(new JLabel("Spot Height:"));
        controlPanel.add(spotHeight);
        controlPanel.add(new JLabel("Spot Width:"));
        controlPanel.add(spotWidth);
        controlPanel.add(new JLabel("Spot Space:"));
        controlPanel.add(spotSpace);
        controlPanel.add(new Label("Page Size:"));
        controlPanel.add(pageSizeJComboBox);
        rebuildPreviewImage();
        return controlPanel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        rebuildPreviewImage();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        rebuildPreviewImage();
    }

    private void rebuildPreviewImage() {
        Integer height = Integer.parseInt(String.valueOf(spotHeight.getValue()));
        Integer width = Integer.parseInt(String.valueOf(spotWidth.getValue()));
        Integer space = Integer.parseInt(String.valueOf(spotSpace.getValue()));
        PageSize pageSize = (PageSize) pageSizeJComboBox.getSelectedItem();
        previewWindow.refresh(height, width, space, pageSize);
    }
}
