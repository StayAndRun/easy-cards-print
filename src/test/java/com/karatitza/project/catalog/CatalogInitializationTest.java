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
        String svgCatalogPath = "./src/test/resources/svg-project/source";
        DecksCatalog svgCatalog = new DecksCatalog(new File(svgCatalogPath), ImageFormat.SVG);
        List<Deck> decks = svgCatalog.getDecks();
        Assertions.assertEquals(5, decks.size());

        Deck blueDeck = searchDeckByName(decks, "blue-deck");
        List<Card> blueCards = blueDeck.getCards();
        Assertions.assertEquals(2, blueCards.size());
        Card bird = searchCardByName(blueCards, "bird.svg");
        Assertions.assertEquals("blue-back.svg", bird.getBackSide().get().getName());
        Card elemental = searchCardByName(blueCards, "elemental.svg");
        Assertions.assertEquals("blue-back.svg", elemental.getBackSide().get().getName());

        Deck greenDeck = searchDeckByName(decks, "green-deck");
        List<Card> greenCards = greenDeck.getCards();
        Assertions.assertEquals(2, greenDeck.getCards().size());
        Card wolf = searchCardByName(greenCards, "wolf.svg");
        Assertions.assertEquals("green-back.svg", wolf.getBackSide().get().getName());
        Card spider = searchCardByName(greenCards, "spider.svg");
        Assertions.assertEquals("green-back.svg", spider.getBackSide().get().getName());

        Deck redDeck = searchDeckByName(decks, "red-deck");
        List<Card> redCards = redDeck.getCards();
        Assertions.assertEquals(2, redDeck.getCards().size());
        Card goblin = searchCardByName(redCards, "goblin.svg");
        Assertions.assertEquals("red-back.svg", goblin.getBackSide().get().getName());
        Card dragon = searchCardByName(redCards, "dragon.svg");
        Assertions.assertEquals("red-back.svg", dragon.getBackSide().get().getName());

        Deck blackDeck = searchDeckByName(decks, "black-deck");
        List<Card> blackCards = blackDeck.getCards();
        Assertions.assertEquals(2, blackDeck.getCards().size());
        Card zombie = searchCardByName(blackCards, "zombie.svg");
        Assertions.assertEquals("black-back.svg", zombie.getBackSide().get().getName());
        Card skeleton = searchCardByName(blackCards, "skeleton.svg");
        Assertions.assertEquals("black-back.svg", skeleton.getBackSide().get().getName());

        Deck yellowDeck = searchDeckByName(decks, "white-deck");
        List<Card> yellowCards = yellowDeck.getCards();
        Assertions.assertEquals(1, yellowDeck.getCards().size());
        Card knight = searchCardByName(yellowCards, "knight.svg");
        Assertions.assertEquals("white-back.svg", knight.getBackSide().get().getName());
    }

    @Test
    void acceptInitPdfCatalog() {
        String svgCatalogPath = "./src/test/resources/pdf-project/source";
        DecksCatalog svgCatalog = new DecksCatalog(new File(svgCatalogPath), ImageFormat.PDF);
        List<Deck> decks = svgCatalog.getDecks();
        Assertions.assertEquals(5, decks.size());

        Deck blueDeck = searchDeckByName(decks, "blue-deck");
        List<Card> blueCards = blueDeck.getCards();
        Assertions.assertEquals(2, blueCards.size());
        Card bird = searchCardByName(blueCards, "bird.pdf");
        Assertions.assertEquals("blue-back.pdf", bird.getBackSide().get().getName());
        Card elemental = searchCardByName(blueCards, "elemental.pdf");
        Assertions.assertEquals("blue-back.pdf", elemental.getBackSide().get().getName());

        Deck greenDeck = searchDeckByName(decks, "green-deck");
        List<Card> greenCards = greenDeck.getCards();
        Assertions.assertEquals(2, greenDeck.getCards().size());
        Card wolf = searchCardByName(greenCards, "wolf.pdf");
        Assertions.assertEquals("green-back.pdf", wolf.getBackSide().get().getName());
        Card spider = searchCardByName(greenCards, "spider.pdf");
        Assertions.assertEquals("green-back.pdf", spider.getBackSide().get().getName());

        Deck redDeck = searchDeckByName(decks, "red-deck");
        List<Card> redCards = redDeck.getCards();
        Assertions.assertEquals(2, redDeck.getCards().size());
        Card goblin = searchCardByName(redCards, "goblin.pdf");
        Assertions.assertEquals("red-back.pdf", goblin.getBackSide().get().getName());
        Card dragon = searchCardByName(redCards, "dragon.pdf");
        Assertions.assertEquals("red-back.pdf", dragon.getBackSide().get().getName());

        Deck blackDeck = searchDeckByName(decks, "black-deck");
        List<Card> blackCards = blackDeck.getCards();
        Assertions.assertEquals(2, blackDeck.getCards().size());
        Card zombie = searchCardByName(blackCards, "zombie.pdf");
        Assertions.assertEquals("black-back.pdf", zombie.getBackSide().get().getName());
        Card skeleton = searchCardByName(blackCards, "skeleton.pdf");
        Assertions.assertEquals("black-back.pdf", skeleton.getBackSide().get().getName());

        Deck yellowDeck = searchDeckByName(decks, "white-deck");
        List<Card> yellowCards = yellowDeck.getCards();
        Assertions.assertEquals(1, yellowDeck.getCards().size());
        Card knight = searchCardByName(yellowCards, "knight.pdf");
        Assertions.assertEquals("white-back.pdf", knight.getBackSide().get().getName());
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
