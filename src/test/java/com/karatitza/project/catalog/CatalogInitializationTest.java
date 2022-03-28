package com.karatitza.project.catalog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CatalogInitializationTest {

    @Test
    void acceptInitSvgCatalog() {
        String svgCatalogPath = "./src/test/resources/testProject/source";
        DecksCatalog svgCatalog = new DecksCatalog(new File(svgCatalogPath), ImageFormat.SVG);
        List<Deck> decks = svgCatalog.getDecks();
        Assertions.assertEquals(4, decks.size());

        Deck blueDeck = searchDeckByName(decks, "deck-blue");
        List<Card> blueCards = blueDeck.getCards();
        Assertions.assertEquals(2, blueCards.size());
        Card card5 = searchCardByName(blueCards, "card5.svg");
        Assertions.assertEquals("back-blue.svg", card5.getBackSide().get().getName());
        Card card6 = searchCardByName(blueCards, "card6.svg");
        Assertions.assertEquals("back-blue.svg", card6.getBackSide().get().getName());

        Deck greenDeck = searchDeckByName(decks, "deck-green");
        List<Card> greenCards = greenDeck.getCards();
        Assertions.assertEquals(2, greenDeck.getCards().size());
        Card card1 = searchCardByName(greenCards, "card1.svg");
        Assertions.assertEquals("back-green.svg", card1.getBackSide().get().getName());
        Card card2 = searchCardByName(greenCards, "card2.svg");
        Assertions.assertEquals("back-green.svg", card2.getBackSide().get().getName());

        Deck redDeck = searchDeckByName(decks, "deck-red");
        List<Card> redCards = redDeck.getCards();
        Assertions.assertEquals(2, redDeck.getCards().size());
        Card card3 = searchCardByName(redCards, "card3.svg");
        Assertions.assertEquals("back-red.svg", card3.getBackSide().get().getName());
        Card card4 = searchCardByName(redCards, "card4.svg");
        Assertions.assertEquals("back-red.svg", card4.getBackSide().get().getName());

        Deck yellowDeck = searchDeckByName(decks, "deck-yellow");
        List<Card> yellowCards = yellowDeck.getCards();
        Assertions.assertEquals(3, yellowDeck.getCards().size());
        Card card7 = searchCardByName(yellowCards, "card7.svg");
        Assertions.assertEquals("back-yellow.svg", card7.getBackSide().get().getName());
        Card card8 = searchCardByName(yellowCards, "card8.svg");
        Assertions.assertEquals("back-yellow.svg", card8.getBackSide().get().getName());
        Card card9 = searchCardByName(yellowCards, "card9.svg");
        Assertions.assertEquals("back-yellow.svg", card9.getBackSide().get().getName());
    }

    private Deck searchDeckByName(List<Deck> decks, String name) {
        Optional<Deck> foundDeck = decks.stream().filter(deck -> deck.getName().equals(name)).findFirst();
        return foundDeck.orElseThrow(() -> new AssertionFailedError("Not found deck: " + name));
    }

    private Card searchCardByName(List<Card> cards, String name) {
        Optional<Card> foundDeck = cards.stream()
                .filter(card -> card.getFrontSide().getName().equals(name)).findFirst();
        return foundDeck.orElseThrow(() -> new AssertionFailedError("Not found card: " + name));
    }
}
