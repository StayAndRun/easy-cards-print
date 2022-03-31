package com.karatitza.project.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ControlWindow implements ChangeListener {

    private final PreviewWindow previewWindow;
    private final JSpinner spotHeight;
    private final JSpinner spotWidth;
    private final JSpinner spotSpace;

    public ControlWindow(PreviewWindow previewWindow) {
        this.previewWindow = previewWindow;
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
        previewWindow.refresh(height, width, space);
    }
}
