package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.catalog.Card;
import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.layout.cards.CardsLayout;
import com.karatitza.project.layout.spots.SpotSize;

public class LayoutComposer {

    private final DocumentLayout documentLayout;

    public LayoutComposer(PageSize pageSize, SpotSize spotSize) {
        this.documentLayout = new DocumentLayout(pageSize, spotSize);
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
        return documentLayout;
    }

    private CardsLayout nextPaperLayout() {
        return documentLayout.createPage();
    }
}
