package com.karatitza.gui.swing.panels;

import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;

import javax.swing.*;

public class CatalogPreviewPanel extends JPanel{

    private final JTextArea catalogInfo;

    public CatalogPreviewPanel() {
        catalogInfo = new JTextArea("No data");
        catalogInfo.setEditable(false);
        setBorder(BorderFactory.createTitledBorder("Selected catalog info"));
        add(catalogInfo);
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
