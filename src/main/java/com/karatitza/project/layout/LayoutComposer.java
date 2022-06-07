package com.karatitza.project.layout;

import com.karatitza.project.catalog.Card;
import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.layout.cards.CardsLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LayoutComposer {
    public static final Logger LOG = LoggerFactory.getLogger(LayoutComposer.class);

    private final DocumentLayout documentLayout;

    public LayoutComposer(DocumentLayout documentLayout) {
        this.documentLayout = documentLayout;
    }

    public DocumentLayout compose(DecksCatalog decksCatalog) {
        CardsLayout currentCardsLayout = nextPaperLayout();
        for (Deck deck : decksCatalog.getDecks()) {
            for (Card card : deck.getCards()) {
                if (currentCardsLayout.isFilled()) {
                    currentCardsLayout = nextPaperLayout();
                }
                currentCardsLayout.place(card);
            }
        }
        LOG.info("Composed document layout, total pages: {}", documentLayout.size());
        return documentLayout;
    }

    private CardsLayout nextPaperLayout() {
        return documentLayout.createPage();
    }
}
