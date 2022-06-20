package com.karatitza.gui.swing.panels;

import com.karatitza.gui.swing.panels.checkbox.CheckBoxCatalogTree;
import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class CatalogPreviewPanel extends JPanel {

    private final JTextArea catalogInfo;
    private final DecksCatalog catalog;


    public CatalogPreviewPanel(DecksCatalog catalog) {
        setLayout(new GridLayout(1, 2));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Selected catalog stats")));
        this.catalog = catalog;
        this.catalogInfo = emptyCatalogInfo();
        add(catalogInfo);
        add(new CheckBoxCatalogTree(catalog));
        setCatalogStats();
    }

    private JTextArea emptyCatalogInfo() {
        final JTextArea catalogInfo;
        catalogInfo = new JTextArea("No data");
        catalogInfo.setEditable(false);
        return catalogInfo;
    }

    public void setCatalogStats() {
        StringBuilder infoBuilder = new StringBuilder();
        for (Deck deck : catalog.getDecks()) {
            infoBuilder.append(deck.getName())
                    .append(" (cards: ").append(deck.getCards().size()).append(")")
                    .append('\n');
        }
        infoBuilder.append('\n').append("Total decks: ").append(catalog.getDecks().size());
        catalogInfo.setText(infoBuilder.toString());
    }
}
