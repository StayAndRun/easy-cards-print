package com.karatitza.project.gui;

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
        JPanel controlPanel = new ControlWindow(previewWindow).packToPanel();

        mainPanel.add(previewPanel);
        mainPanel.add(controlPanel);

        frame.setTitle("Easy Card Print");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
