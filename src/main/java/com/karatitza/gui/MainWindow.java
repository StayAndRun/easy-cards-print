package com.karatitza.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    public static void main(String[] args) {
        new MainWindow().open();
    }

    public void open() {
        JFrame frame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new GridLayout(1, 2));

        PreviewWindow previewWindow = new PreviewWindow();
        JPanel previewPanel = previewWindow.packToPanel();
        JPanel spotControlPanel = new SpotControlWindow(previewWindow).packToPanel();

        JPanel catalogControlPanel = new JPanel(new GridLayout(0, 1));
        catalogControlPanel.add(new Button("Build PDF"));

        JPanel controlPanel = new JPanel(new GridLayout(2,1));

        controlPanel.add(spotControlPanel);
        controlPanel.add(catalogControlPanel);

        mainPanel.add(previewPanel);
        mainPanel.add(controlPanel);

        frame.setTitle("Easy Card Print");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
