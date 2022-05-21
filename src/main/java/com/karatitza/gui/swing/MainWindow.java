package com.karatitza.gui.swing;

import com.karatitza.gui.swing.areas.CardCatalogControlArea;
import com.karatitza.gui.swing.areas.SpotControlArea;
import com.karatitza.gui.swing.areas.SpotsLayoutPreviewArea;
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new GridLayout(1, 2));

        SpotsLayoutPreviewArea spotsLayoutPreviewArea = new SpotsLayoutPreviewArea(cardProject);
        JPanel layoutPreviewPanel = spotsLayoutPreviewArea.packToPanel();
        JPanel spotControlPanel = new SpotControlArea(spotsLayoutPreviewArea).packToPanel();
        JPanel catalogControlPanel = new CardCatalogControlArea(cardProject).packToPanel();

        JPanel controlPanel = new JPanel(new GridLayout(2,1));

        controlPanel.add(spotControlPanel);
        controlPanel.add(catalogControlPanel);

        mainPanel.add(layoutPreviewPanel);
        mainPanel.add(controlPanel);

        frame.setTitle("Easy Card Print");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
