package com.karatitza.project.catalog;

import com.karatitza.converters.ConversionTask;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.converters.itext.ITextSvgToPdfConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.List;
import java.util.Optional;

class CatalogConversionTest {

    public static final String TEST_PROJECT_PATH = "./src/test/resources/test-project";
    public static final String TEST_SOURCE_PATH = "./src/test/resources/test-project/source";
    public static final String TEST_TEMP_PATH = "./src/test/resources/test-project/print/temp/pdf";

    @Test
    void acceptSourceToPdfConversion() {
        DecksCatalog sourceCatalog = new DecksCatalog(new File(TEST_SOURCE_PATH), ImageFormat.SVG);
        sourceCatalog.convert(
                new ITextSvgToPdfConverter(new TempImageFactory(new File(TEST_PROJECT_PATH)))
        );

        DecksCatalog targetCatalog = new DecksCatalog(new File(TEST_TEMP_PATH), ImageFormat.PDF);
        List<Deck> decks = targetCatalog.getDecks();
        Assertions.assertEquals(4, decks.size());

        Deck blueDeck = searchDeckByName(decks, "deck-blue");
        List<Card> blueCards = blueDeck.getCards();
        Assertions.assertEquals(2, blueCards.size());
        Card card5 = searchCardByName(blueCards, "card5.pdf");
        Assertions.assertEquals("back-blue.pdf", card5.getBackSide().get().getName());
        Card card6 = searchCardByName(blueCards, "card6.pdf");
        Assertions.assertEquals("back-blue.pdf", card6.getBackSide().get().getName());

        Deck greenDeck = searchDeckByName(decks, "deck-green");
        List<Card> greenCards = greenDeck.getCards();
        Assertions.assertEquals(2, greenDeck.getCards().size());
        Card card1 = searchCardByName(greenCards, "card1.pdf");
        Assertions.assertEquals("back-green.pdf", card1.getBackSide().get().getName());
        Card card2 = searchCardByName(greenCards, "card2.pdf");
        Assertions.assertEquals("back-green.pdf", card2.getBackSide().get().getName());

        Deck redDeck = searchDeckByName(decks, "deck-red");
        List<Card> redCards = redDeck.getCards();
        Assertions.assertEquals(2, redDeck.getCards().size());
        Card card3 = searchCardByName(redCards, "card3.pdf");
        Assertions.assertEquals("back-red.pdf", card3.getBackSide().get().getName());
        Card card4 = searchCardByName(redCards, "card4.pdf");
        Assertions.assertEquals("back-red.pdf", card4.getBackSide().get().getName());

        Deck yellowDeck = searchDeckByName(decks, "deck-yellow");
        List<Card> yellowCards = yellowDeck.getCards();
        Assertions.assertEquals(3, yellowDeck.getCards().size());
        Card card7 = searchCardByName(yellowCards, "card7.pdf");
        Assertions.assertEquals("back-yellow.pdf", card7.getBackSide().get().getName());
        Card card8 = searchCardByName(yellowCards, "card8.pdf");
        Assertions.assertEquals("back-yellow.pdf", card8.getBackSide().get().getName());
        Card card9 = searchCardByName(yellowCards, "card9.pdf");
        Assertions.assertEquals("back-yellow.pdf", card9.getBackSide().get().getName());
    }

    @Test
    void acceptDoubleConversion() {
    }

    @BeforeEach
    void setUp() {
        File tempDir = new File(TEST_PROJECT_PATH + ConversionTask.TEMP_FILES_RELATE_PATH);
        deleteSubDirs(tempDir);
        if (tempDir.exists() && !tempDir.delete()) {
            throw new TestInstantiationException("Failed to remove temp files: " + tempDir);
        }
    }

    private void deleteSubDirs(File tempDir) {
        File[] subFiles = tempDir.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                deleteSubDirs(subFile);
                subFile.delete();
            }
        }
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