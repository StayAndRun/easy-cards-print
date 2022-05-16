package com.karatitza.gui;

import com.karatitza.project.CardProject;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private final CardProject cardProject;

    public MainWindow(CardProject cardProject) {
        this.cardProject = cardProject;
    }

    public static void main(String[] args) {
        new MainWindow(new CardProject()).open();
    }

    public void open() {
        JFrame frame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new GridLayout(1, 2));

        PreviewWindow previewWindow = new PreviewWindow(cardProject);
        JPanel previewPanel = previewWindow.packToPanel();
        JPanel spotControlPanel = new SpotControlWindow(previewWindow).packToPanel();
        JPanel catalogControlPanel = new CardCatalogControlWindow(cardProject).packToPanel();

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
