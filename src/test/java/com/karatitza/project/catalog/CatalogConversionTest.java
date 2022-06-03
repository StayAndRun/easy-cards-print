package com.karatitza.project.catalog;

import com.karatitza.converters.TempFileProvider;
import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.converters.itext.ITextSvgToPdfConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

class CatalogConversionTest extends TempFilesTest {

    public static final String TEST_PROJECT_PATH = "./src/test/resources/svg-project";
    public static final String TEST_SOURCE_PATH = "./src/test/resources/svg-project/decks";
    public static final String TEST_MULTI_CATALOG_PATH = "./src/test/resources/multi-project/decks";
    public static final String TEST_MULTI_PROJECT_PATH = "./src/test/resources/multi-project";
    public static final String TEST_MULTI_PROJECT_TEMP_PATH = "./src/test/resources/multi-project/print/temp/PDF";
    public static final String TEST_TEMP_PATH = "./src/test/resources/svg-project/print/temp/PDF";

    @Test
    void acceptSourceToPdfConversion() {
        DecksCatalog sourceCatalog = new DecksCatalog(new File(TEST_SOURCE_PATH));
        ITextSvgToPdfConverter converter = new ITextSvgToPdfConverter(new TempFileProvider(new File(TEST_PROJECT_PATH)));
        Deque<Integer> conversionProgressSteps = new ArrayDeque<>();
        sourceCatalog.convert(converter, conversionProgressSteps::add);
        Assertions.assertEquals(100, conversionProgressSteps.getLast());
        assertCreatedFilesCreated(TEST_TEMP_PATH);
    }

    @Test
    void acceptInkscapeConversion() {
        DecksCatalog sourceCatalog = new DecksCatalog(new File(TEST_SOURCE_PATH));
        InkscapeSvgToPdfConverter converter = new InkscapeSvgToPdfConverter(new TempFileProvider(new File(TEST_PROJECT_PATH)));
        Deque<Integer> conversionProgressSteps = new ArrayDeque<>();
        sourceCatalog.convert(
                converter, conversionProgressSteps::add
        );
        Assertions.assertEquals(100, conversionProgressSteps.getLast());
        assertCreatedFilesCreated(TEST_TEMP_PATH);
    }

    @Test
    void acceptMultiCatalogConversion() {
        DecksCatalog sourceCatalog = new DecksCatalog(new File(TEST_MULTI_CATALOG_PATH));
        ITextSvgToPdfConverter converter = new ITextSvgToPdfConverter(new TempFileProvider(new File(TEST_MULTI_PROJECT_PATH)));
        Deque<Integer> conversionProgressSteps = new ArrayDeque<>();
        sourceCatalog.convert(converter, conversionProgressSteps::add);
        Assertions.assertEquals(100, conversionProgressSteps.getLast());
        assertCreatedFilesCreated(TEST_MULTI_PROJECT_TEMP_PATH);
    }

    private void assertCreatedFilesCreated(String path) {
        DecksCatalog targetCatalog = new DecksCatalog(new File(path));
        List<Deck> decks = targetCatalog.getDecks();
        Assertions.assertEquals(5, decks.size());

        Deck blueDeck = searchDeckByName(decks, "blue-deck");
        List<Card> blueCards = blueDeck.getCards();
        Assertions.assertEquals(2, blueCards.size());
        Card bird = searchCardByName(blueCards, "bird.pdf");
        Assertions.assertEquals("blue back.pdf", acceptBackSide(bird).getName());
        Card elemental = searchCardByName(blueCards, "elemental.pdf");
        Assertions.assertEquals("blue back.pdf", acceptBackSide(elemental).getName());

        Deck greenDeck = searchDeckByName(decks, "green-deck");
        List<Card> greenCards = greenDeck.getCards();
        Assertions.assertEquals(2, greenDeck.getCards().size());
        Card wolf = searchCardByName(greenCards, "wolf.pdf");
        Assertions.assertEquals("green back.pdf", acceptBackSide(wolf).getName());
        Card spider = searchCardByName(greenCards, "spider.pdf");
        Assertions.assertEquals("green back.pdf", acceptBackSide(spider).getName());

        Deck redDeck = searchDeckByName(decks, "red-deck");
        List<Card> redCards = redDeck.getCards();
        Assertions.assertEquals(2, redDeck.getCards().size());
        Card goblin = searchCardByName(redCards, "goblin.pdf");
        Assertions.assertEquals("red back.pdf", acceptBackSide(goblin).getName());
        Card dragon = searchCardByName(redCards, "dragon.pdf");
        Assertions.assertEquals("red back.pdf", acceptBackSide(dragon).getName());

        Deck blackDeck = searchDeckByName(decks, "black-deck");
        List<Card> blackCards = blackDeck.getCards();
        Assertions.assertEquals(2, blackDeck.getCards().size());
        Card zombie = searchCardByName(blackCards, "zombie.pdf");
        Assertions.assertEquals("black back.pdf", acceptBackSide(zombie).getName());
        Card skeleton = searchCardByName(blackCards, "skeleton.pdf");
        Assertions.assertEquals("black back.pdf", acceptBackSide(skeleton).getName());

        Deck yellowDeck = searchDeckByName(decks, "white-deck");
        List<Card> yellowCards = yellowDeck.getCards();
        Assertions.assertEquals(1, yellowDeck.getCards().size());
        Card knight = searchCardByName(yellowCards, "knight.pdf");
        Assertions.assertEquals("white back.pdf", acceptBackSide(knight).getName());
    }

    private Image acceptBackSide(Card card) {
        if (card.getBackSide().isPresent()) {
            return card.getBackSide().get();
        } else {
           throw new AssertionFailedError("Not found back side image for card: " + card);
        }
    }

    @BeforeEach
    void setUp() {
        cleanupProjectTempFiles(TEST_PROJECT_PATH);
        cleanupProjectTempFiles(TEST_MULTI_CATALOG_PATH);
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