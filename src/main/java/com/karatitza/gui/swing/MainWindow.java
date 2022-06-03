package com.karatitza.gui.swing;

import com.karatitza.gui.swing.panels.CardCatalogControlPanel;
import com.karatitza.gui.swing.panels.SpotControlPanel;
import com.karatitza.gui.swing.panels.SpotsLayoutPreviewPanel;
import com.karatitza.project.CardProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {

    private CardProjectWindow currentProjectWindow;

    public static void main(String[] args) {
        new MainWindow().open();
    }

    public void open() {
        currentProjectWindow = new CardProjectWindow(new CardProject());
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(openProjectButton());
        menuBar.add(selectLatestProjectMenu());
        setJMenuBar(menuBar);
        setTitle("Easy Card Print");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(currentProjectWindow, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private JMenu selectLatestProjectMenu() {
        JMenu menu = new JMenu("Select latest project");
        menu.add(buildLatestProjectItem(new File("C:\\GitRepo\\EasyCardsPrint\\src\\test\\resources\\svg-project")));
        menu.add(buildLatestProjectItem(new File("C:\\GitRepo\\EasyCardsPrint\\src\\test\\resources\\pdf-project")));
        return menu;
    }

    private JMenuItem buildLatestProjectItem(File projectRoot) {
        JMenuItem menuItem = new JMenuItem(projectRoot.getName());
        menuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProjectWindow(projectRoot);
            }
        });
        return menuItem;
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
                    openProjectWindow(projectChooser.getSelectedFile());
                }
            }
        });
        return open;
    }

    private void openProjectWindow(File projectRoot) {
        CardProject openedProject = CardProject.openFromDir(projectRoot);
        JPanel openedProjectWindow = new CardProjectWindow(openedProject);
        currentProjectWindow.setVisible(false);
        MainWindow.this.remove(currentProjectWindow);
        MainWindow.this.add(openedProjectWindow);
    }

    public static class CardProjectWindow extends JPanel{

        public CardProjectWindow(CardProject cardProject) {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setLayout(new GridLayout(1, 2));

            SpotsLayoutPreviewPanel layoutPreviewPanel = new SpotsLayoutPreviewPanel(cardProject);
            SpotControlPanel spotControlPanel = new SpotControlPanel();
            spotControlPanel.setSpotsLayoutPreview(layoutPreviewPanel);
            spotControlPanel.setInitialSpots(cardProject.getSpotsLayout());
            JPanel catalogControlPanel = new CardCatalogControlPanel(cardProject);

            JPanel controlPanel = new JPanel(new GridLayout(2, 1));

            controlPanel.add(spotControlPanel);
            controlPanel.add(catalogControlPanel);

            add(layoutPreviewPanel);
            add(controlPanel);
        }
    }

}
