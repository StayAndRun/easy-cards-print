package com.karatitza.gui.swing;

import com.karatitza.gui.swing.logging.LogPaneSingleton;
import com.karatitza.gui.swing.panels.CardsCatalogControlPanel;
import com.karatitza.gui.swing.panels.PdfBuildControlPanel;
import com.karatitza.gui.swing.panels.SpotControlPanel;
import com.karatitza.gui.swing.panels.SpotsLayoutPreviewPanel;
import com.karatitza.project.CardProject;
import com.karatitza.project.LatestProjectsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {
    public static final Logger LOG = LoggerFactory.getLogger(MainWindow.class);

    private final JPanel currentProjectWindowTab = new JPanel(new BorderLayout());

    public static void main(String[] args) {
        new MainWindow().open();
    }

    public void open() {
        setSystemView();
        setTitle("Easy Card Print");
        setPreferredSize(new Dimension(1500, 1000));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(openProjectButton());
        menuBar.add(buildLatestProjectMenu());
        addTabs();
        setJMenuBar(menuBar);
        openProjectWindow(new CardProject());
        pack();
        setVisible(true);
    }

    private void setSystemView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.warn("Failed to set System UI view: ", e);
        }
    }

    private void addTabs() {
        final JTabbedPane tabs = new JTabbedPane(SwingConstants.LEFT);
        tabs.addTab("Current project", currentProjectWindowTab);
        tabs.addTab("Debug logs", new JScrollPane(LogPaneSingleton.getInstance()));
        add(tabs);
    }

    private JMenu buildLatestProjectMenu() {
        JMenu menu = new JMenu("Select latest project");
        LatestProjectsConfig config = LatestProjectsConfig.loadFromFile();
        for (File latestProject : config.getLatestProjects()) {
            menu.add(buildLatestProjectItem(latestProject));
        }
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

    private CardProjectWindow openProjectWindow(File projectRoot) {
        return openProjectWindow(CardProject.openFromDir(projectRoot));
    }

    private CardProjectWindow openProjectWindow(CardProject openedProject) {
        CardProjectWindow openedProjectWindow = new CardProjectWindow(openedProject);
        currentProjectWindowTab.removeAll();
        currentProjectWindowTab.setSize(new Dimension(1000, 1000));
        currentProjectWindowTab.add(openedProjectWindow, BorderLayout.CENTER);
        return openedProjectWindow;
    }

    public static class CardProjectWindow extends JPanel {

        public CardProjectWindow(CardProject cardProject) {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setLayout(new GridLayout(1, 2));

            SpotsLayoutPreviewPanel layoutPreviewPanel = new SpotsLayoutPreviewPanel(cardProject);
            SpotControlPanel spotControlPanel = new SpotControlPanel(cardProject);
            spotControlPanel.addLayoutsChangeListener(layoutPreviewPanel);
            JPanel pdfControlPanel = new PdfBuildControlPanel(cardProject);
            CardsCatalogControlPanel catalogControlPanel = new CardsCatalogControlPanel(cardProject.getSelectedCatalog());

            JPanel controlPanel = new JPanel(new GridLayout(3, 1));

            controlPanel.add(spotControlPanel);
            controlPanel.add(catalogControlPanel);
            controlPanel.add(pdfControlPanel);

            add(layoutPreviewPanel);
            add(controlPanel);
        }
    }

}
