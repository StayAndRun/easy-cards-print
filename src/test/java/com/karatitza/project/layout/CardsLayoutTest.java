package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.catalog.Card;
import com.karatitza.project.catalog.Deck;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.layout.cards.ImageLayout;
import com.karatitza.project.layout.spots.SpotSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CardsLayoutTest {
    // _______________________________________________
    // 2     green-spider     green-wolf  black-zombie
    // 1     black-skeleton   blue-bird   blue-elemental
    // 0     red-dragon       red-goblin  white-knight
    // -----------------------------------------------
    //            0             1           2
    // _______________________________________________
    // 2        black         green       green
    // 1        blue          blue        black
    // 0        white         red         red
    // -----------------------------------------------
    //            0             1           2
    @Test
    public void acceptPageLayout3on3() {
        LayoutComposer composer = createComposer(PageSize.A4, SpotSize.millimeters(92, 59));
        DocumentLayout pages = composer.compose(mockDecksCatalog());
        Assertions.assertEquals(1, pages.size());
        ImageLayout frontPage = pages.get(0).getFrontLayout();
        Assertions.assertEquals("green-spider", frontPage.getImage(0, 2).toString());
        Assertions.assertEquals("green-wolf", frontPage.getImage(1, 2).toString());
        Assertions.assertEquals("black-zombie", frontPage.getImage(2, 2).toString());
        Assertions.assertEquals("black-skeleton", frontPage.getImage(0, 1).toString());
        Assertions.assertEquals("blue-bird", frontPage.getImage(1, 1).toString());
        Assertions.assertEquals("blue-elemental", frontPage.getImage(2, 1).toString());
        Assertions.assertEquals("red-dragon", frontPage.getImage(0, 0).toString());
        Assertions.assertEquals("red-goblin", frontPage.getImage(1, 0).toString());
        Assertions.assertEquals("white-knight", frontPage.getImage(2, 0).toString());
        ImageLayout backPage = pages.get(0).getBackLayout();
        Assertions.assertEquals("black", backPage.getImage(0, 2).toString());
        Assertions.assertEquals("green", backPage.getImage(1, 2).toString());
        Assertions.assertEquals("green", backPage.getImage(2, 2).toString());
        Assertions.assertEquals("blue", backPage.getImage(0, 1).toString());
        Assertions.assertEquals("blue", backPage.getImage(1, 1).toString());
        Assertions.assertEquals("black", backPage.getImage(2, 1).toString());
        Assertions.assertEquals("white", backPage.getImage(0, 0).toString());
        Assertions.assertEquals("red", backPage.getImage(1, 0).toString());
        Assertions.assertEquals("red", backPage.getImage(2, 0).toString());
    }

    @Test
    public void acceptPageLayout2on2() {
        LayoutComposer composer = createComposer(PageSize.A4, SpotSize.millimeters(120, 100));
        DocumentLayout pages = composer.compose(mockDecksCatalog());
        Assertions.assertEquals(3, pages.size());
        ImageLayout firstFrontPage = pages.get(0).getFrontLayout();
        Assertions.assertEquals(1, firstFrontPage.maxColumnIndex);
        Assertions.assertEquals(1, firstFrontPage.maxRowIndex);
        Assertions.assertEquals("green-spider", firstFrontPage.getImage(0, 1).toString());
        Assertions.assertEquals("green-wolf", firstFrontPage.getImage(1, 1).toString());
        Assertions.assertEquals("black-zombie", firstFrontPage.getImage(0, 0).toString());
        Assertions.assertEquals("black-skeleton", firstFrontPage.getImage(1, 0).toString());
        ImageLayout firstBackPage = pages.get(0).getBackLayout();
        Assertions.assertEquals(1, firstBackPage.maxColumnIndex);
        Assertions.assertEquals(1, firstBackPage.maxRowIndex);
        Assertions.assertEquals("green", firstBackPage.getImage(0, 1).toString());
        Assertions.assertEquals("green", firstBackPage.getImage(1, 1).toString());
        Assertions.assertEquals("black", firstBackPage.getImage(0, 0).toString());
        Assertions.assertEquals("black", firstBackPage.getImage(1, 0).toString());
        ImageLayout secondFrontPage = pages.get(1).getFrontLayout();
        Assertions.assertEquals("blue-bird", secondFrontPage.getImage(0, 1).toString());
        Assertions.assertEquals("blue-elemental", secondFrontPage.getImage(1, 1).toString());
        Assertions.assertEquals("red-dragon", secondFrontPage.getImage(0, 0).toString());
        Assertions.assertEquals("red-goblin", secondFrontPage.getImage(1, 0).toString());
        ImageLayout secondBackPage = pages.get(1).getBackLayout();
        Assertions.assertEquals("blue", secondBackPage.getImage(0, 1).toString());
        Assertions.assertEquals("blue", secondBackPage.getImage(1, 1).toString());
        Assertions.assertEquals("red", secondBackPage.getImage(0, 0).toString());
        Assertions.assertEquals("red", secondBackPage.getImage(1, 0).toString());
        ImageLayout thirdFrontPage = pages.get(2).getFrontLayout();
        Assertions.assertEquals("white-knight", thirdFrontPage.getImage(0, 1).toString());
        ImageLayout thirdBackPage = pages.get(2).getBackLayout();
        Assertions.assertEquals("white", thirdBackPage.getImage(1, 1).toString());
    }

    private LayoutComposer createComposer(PageSize pageSize, SpotSize millimeters) {
        return new LayoutComposer(new DocumentLayout(pageSize, millimeters));
    }

    private DecksCatalog mockDecksCatalog() {
        Deck greenDeck = mockDeck("green",
                mockCard("green-spider", "green"),
                mockCard("green-wolf", "green")
        );
        Deck blackDeck = mockDeck("black",
                mockCard("black-zombie", "black"),
                mockCard("black-skeleton", "black")
        );
        Deck blueDeck = mockDeck("blue",
                mockCard("blue-bird", "blue"),
                mockCard("blue-elemental", "blue")
        );
        Deck redDeck = mockDeck("red",
                mockCard("red-dragon", "red"),
                mockCard("red-goblin", "red")
        );
        Deck whiteDeck = mockDeck("white",
                mockCard("white-knight", "white")
        );
        DecksCatalog decksCatalog = mock(DecksCatalog.class);
        when(decksCatalog.getDecks())
                .thenReturn(Arrays.asList(greenDeck, blackDeck, blueDeck, redDeck, whiteDeck));
        return decksCatalog;
    }

    private Deck mockDeck(String name, Card... cards) {
        Deck deck = mock(Deck.class, name + "-deck");
        when(deck.getCards()).thenReturn(Arrays.asList(cards));
        return deck;
    }

    private Card mockCard(String cardName, String backImageName) {
        Card card = mock(Card.class, cardName);
        Image frontImage = mock(Image.class, cardName);
        when(card.getFrontSide()).thenReturn(frontImage);
        Image backImage = mock(Image.class, backImageName);
        when(card.getBackSide()).thenReturn(Optional.ofNullable(backImage));
        return card;
    }
}