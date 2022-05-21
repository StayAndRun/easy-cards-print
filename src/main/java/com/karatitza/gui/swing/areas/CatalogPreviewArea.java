package com.karatitza.gui.swing.areas;

import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;

import javax.swing.*;

public class CatalogPreviewArea {

    private final JTextArea catalogInfo;

    public CatalogPreviewArea() {
        catalogInfo = new JTextArea("No data");
        catalogInfo.setEditable(false);
    }

    public JPanel packToPanel() {
        JPanel previewAreaPanel = new JPanel();
        previewAreaPanel.setBorder(BorderFactory.createTitledBorder("Selected catalog info"));
        previewAreaPanel.add(catalogInfo);
        return previewAreaPanel;
    }

    public void refresh(DecksCatalog catalog) {
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
