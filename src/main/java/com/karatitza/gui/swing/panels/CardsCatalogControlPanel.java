package com.karatitza.gui.swing.panels;

import com.karatitza.gui.swing.panels.checkbox.CheckBoxCatalogTree;
import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class CardsCatalogControlPanel extends JPanel {

    private final JTextArea catalogInfo;
    private final DecksCatalog catalog;

    public CardsCatalogControlPanel(DecksCatalog catalog) {
        setLayout(new GridLayout(1, 2));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Selected catalog stats")));
        this.catalog = catalog;
        this.catalogInfo = createSelectedCatalogStats();
        add(catalogInfo);
        add(createCheckBoxCatalogTree(catalog));
    }

    private ScrollPane createCheckBoxCatalogTree(DecksCatalog catalog) {
        CheckBoxCatalogTree catalogTree = new CheckBoxCatalogTree(catalog);
        catalogTree.subscribe(selectable -> refreshStats(catalogInfo));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(catalogTree);
        return scrollPane;
    }

    public JTextArea createSelectedCatalogStats() {
        final JTextArea catalogInfo = new JTextArea();
        catalogInfo.setEditable(false);
        catalogInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        refreshStats(catalogInfo);
        return catalogInfo;
    }

    private void refreshStats(JTextArea catalogInfo) {
        StringBuilder infoBuilder = new StringBuilder("Selected Catalog Statistic: \n\n");
        DecksCatalog selectedCatalog = this.catalog.selectedCatalog();
        for (Deck deck : selectedCatalog.getDecks()) {
            infoBuilder.append(" * ")
                    .append(deck.getName())
                    .append(" (cards: ").append(deck.getCards().size()).append(")")
                    .append('\n');
        }
        infoBuilder.append('\n').append("Total decks: ")
                .append(selectedCatalog.getDecks().size());
        infoBuilder.append('\n').append("Total cards: ")
                .append(selectedCatalog.getDecks().stream().mapToLong(deck -> deck.getCards().size()).sum());
        catalogInfo.setText(infoBuilder.toString());
    }
}
