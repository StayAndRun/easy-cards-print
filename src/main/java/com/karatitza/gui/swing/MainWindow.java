package com.karatitza.gui.swing;

import com.karatitza.gui.swing.areas.CardCatalogControlArea;
import com.karatitza.gui.swing.areas.SpotControlArea;
import com.karatitza.gui.swing.areas.SpotsLayoutPreviewArea;
import com.karatitza.project.CardProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {

    private JPanel currentProjectPanel;

    public static void main(String[] args) {
        new MainWindow().open();
    }

    public void open() {
        currentProjectPanel = new CardProjectWindow(new CardProject()).open();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(openProjectButton());
        setJMenuBar(menuBar);
        setTitle("Easy Card Print");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(currentProjectPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private JButton openProjectButton() {
        JButton open = new JButton("Open card project");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser projectChooser = new JFileChooser();
                projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int selectedOption = projectChooser.showOpenDialog(MainWindow.this);
                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = projectChooser.getSelectedFile();
                    CardProject openedProject = CardProject.openFromDir(selectedFile);
                    JPanel openedProjectWindow = new CardProjectWindow(openedProject).open();
                    currentProjectPanel.setVisible(false);
                    MainWindow.this.remove(currentProjectPanel);
                    MainWindow.this.add(openedProjectWindow);
                }
            }
        });
        return open;
    }

    public static class CardProjectWindow extends JPanel{

        private final CardProject cardProject;

        public CardProjectWindow(CardProject cardProject) {
            this.cardProject = cardProject;
        }

        public JPanel open() {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setLayout(new GridLayout(1, 2));

            SpotsLayoutPreviewArea spotsLayoutPreviewArea = new SpotsLayoutPreviewArea(cardProject);
            JPanel layoutPreviewPanel = spotsLayoutPreviewArea.packToPanel();
            JPanel spotControlPanel = new SpotControlArea(spotsLayoutPreviewArea).packToPanel();
            JPanel catalogControlPanel = new CardCatalogControlArea(cardProject).packToPanel();

            JPanel controlPanel = new JPanel(new GridLayout(2, 1));

            controlPanel.add(spotControlPanel);
            controlPanel.add(catalogControlPanel);

            add(layoutPreviewPanel);
            add(controlPanel);
            return this;
        }
    }

}
