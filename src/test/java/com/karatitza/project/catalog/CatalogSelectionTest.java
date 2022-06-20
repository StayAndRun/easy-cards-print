package com.karatitza.project.catalog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.util.List;

public class CatalogSelectionTest {

    @Test
    void acceptAllImagesSelectedByDefault() {
        String catalogPath = "./src/test/resources/svg-project/decks";
        Selectable catalog = new DecksCatalog(new File(catalogPath));
        Assertions.assertTrue(catalog.isSelected(), "Root Catalog should selected by default!");

        List<? extends Selectable> decks = catalog.children();
        Assertions.assertEquals(5, decks.size());

        Selectable blueDeck = searchSelectableByName(decks, "blue-deck");
        Selectable bird = searchSelectableByName(blueDeck.children(), "bird.svg");
        Selectable elemental = searchSelectableByName(blueDeck.children(), "elemental.svg");
        Selectable blueBacks = searchSelectableByName(blueDeck.children(), "backs");
        Selectable blueBack = searchSelectableByName(blueBacks.children(), "blue back.svg");
        Assertions.assertTrue(blueDeck.isSelected());
        Assertions.assertTrue(bird.isSelected());
        Assertions.assertTrue(elemental.isSelected());
        Assertions.assertTrue(blueBacks.isSelected());
        Assertions.assertTrue(blueBack.isSelected());

        Selectable greenDeck = searchSelectableByName(decks, "green-deck");
        Selectable wolf = searchSelectableByName(greenDeck.children(), "wolf.svg");
        Selectable spider = searchSelectableByName(greenDeck.children(), "spider.svg");
        Selectable greenBacks = searchSelectableByName(greenDeck.children(), "backs");
        Selectable greenBack = searchSelectableByName(greenBacks.children(), "green back.svg");
        Assertions.assertTrue(greenDeck.isSelected());
        Assertions.assertTrue(spider.isSelected());
        Assertions.assertTrue(wolf.isSelected());
        Assertions.assertTrue(greenBacks.isSelected());
        Assertions.assertTrue(greenBack.isSelected());

        Selectable redDeck = searchSelectableByName(decks, "red-deck");
        Selectable goblin = searchSelectableByName(redDeck.children(), "goblin.svg");
        Selectable dragon = searchSelectableByName(redDeck.children(), "dragon.svg");
        Selectable redBacks = searchSelectableByName(redDeck.children(), "backs");
        Selectable redBack = searchSelectableByName(redBacks.children(), "red back.svg");
        Assertions.assertTrue(redDeck.isSelected());
        Assertions.assertTrue(goblin.isSelected());
        Assertions.assertTrue(dragon.isSelected());
        Assertions.assertTrue(redBacks.isSelected());
        Assertions.assertTrue(redBack.isSelected());

        Selectable blackDeck = searchSelectableByName(decks, "black-deck");
        Selectable zombie = searchSelectableByName(blackDeck.children(), "zombie.svg");
        Selectable skeleton = searchSelectableByName(blackDeck.children(), "skeleton.svg");
        Selectable blackBacks = searchSelectableByName(blackDeck.children(), "backs");
        Selectable blackBack = searchSelectableByName(blackBacks.children(), "black back.svg");
        Assertions.assertTrue(blackDeck.isSelected());
        Assertions.assertTrue(zombie.isSelected());
        Assertions.assertTrue(skeleton.isSelected());
        Assertions.assertTrue(blackBacks.isSelected());
        Assertions.assertTrue(blackBack.isSelected());

        Selectable whiteDeck = searchSelectableByName(decks, "white-deck");
        Selectable whiteBacks = searchSelectableByName(whiteDeck.children(), "backs");
        Selectable knight = searchSelectableByName(whiteDeck.children(), "knight.svg");
        Selectable whiteBack = searchSelectableByName(whiteBacks.children(), "white back.svg");
        Assertions.assertTrue(whiteDeck.isSelected());
        Assertions.assertTrue(knight.isSelected());
        Assertions.assertTrue(whiteBacks.isSelected());
        Assertions.assertTrue(whiteBack.isSelected());
    }

    @Test
    void acceptAllImagesUnselectedByRoot() {
        String catalogPath = "./src/test/resources/svg-project/decks";
        Selectable catalog = new DecksCatalog(new File(catalogPath));
        catalog.unselect();
        Assertions.assertFalse(catalog.isSelected(), "Root Catalog should unselected!");

        List<? extends Selectable> decks = catalog.children();
        Assertions.assertEquals(5, decks.size());

        Selectable blueDeck = searchSelectableByName(decks, "blue-deck");
        Selectable bird = searchSelectableByName(blueDeck.children(), "bird.svg");
        Selectable elemental = searchSelectableByName(blueDeck.children(), "elemental.svg");
        Selectable blueBacks = searchSelectableByName(blueDeck.children(), "backs");
        Selectable blueBack = searchSelectableByName(blueBacks.children(), "blue back.svg");
        Assertions.assertFalse(blueDeck.isSelected());
        Assertions.assertFalse(bird.isSelected());
        Assertions.assertFalse(elemental.isSelected());
        Assertions.assertFalse(blueBacks.isSelected());
        Assertions.assertFalse(blueBack.isSelected());

        Selectable greenDeck = searchSelectableByName(decks, "green-deck");
        Selectable wolf = searchSelectableByName(greenDeck.children(), "wolf.svg");
        Selectable spider = searchSelectableByName(greenDeck.children(), "spider.svg");
        Selectable greenBacks = searchSelectableByName(greenDeck.children(), "backs");
        Selectable greenBack = searchSelectableByName(greenBacks.children(), "green back.svg");
        Assertions.assertFalse(greenDeck.isSelected());
        Assertions.assertFalse(spider.isSelected());
        Assertions.assertFalse(wolf.isSelected());
        Assertions.assertFalse(greenBacks.isSelected());
        Assertions.assertFalse(greenBack.isSelected());

        Selectable redDeck = searchSelectableByName(decks, "red-deck");
        Selectable goblin = searchSelectableByName(redDeck.children(), "goblin.svg");
        Selectable dragon = searchSelectableByName(redDeck.children(), "dragon.svg");
        Selectable redBacks = searchSelectableByName(redDeck.children(), "backs");
        Selectable redBack = searchSelectableByName(redBacks.children(), "red back.svg");
        Assertions.assertFalse(redDeck.isSelected());
        Assertions.assertFalse(goblin.isSelected());
        Assertions.assertFalse(dragon.isSelected());
        Assertions.assertFalse(redBacks.isSelected());
        Assertions.assertFalse(redBack.isSelected());

        Selectable blackDeck = searchSelectableByName(decks, "black-deck");
        Selectable zombie = searchSelectableByName(blackDeck.children(), "zombie.svg");
        Selectable skeleton = searchSelectableByName(blackDeck.children(), "skeleton.svg");
        Selectable blackBacks = searchSelectableByName(blackDeck.children(), "backs");
        Selectable blackBack = searchSelectableByName(blackBacks.children(), "black back.svg");
        Assertions.assertFalse(blackDeck.isSelected());
        Assertions.assertFalse(zombie.isSelected());
        Assertions.assertFalse(skeleton.isSelected());
        Assertions.assertFalse(blackBacks.isSelected());
        Assertions.assertFalse(blackBack.isSelected());

        Selectable whiteDeck = searchSelectableByName(decks, "white-deck");
        Selectable whiteBacks = searchSelectableByName(whiteDeck.children(), "backs");
        Selectable knight = searchSelectableByName(whiteDeck.children(), "knight.svg");
        Selectable whiteBack = searchSelectableByName(whiteBacks.children(), "white back.svg");
        Assertions.assertFalse(whiteDeck.isSelected());
        Assertions.assertFalse(knight.isSelected());
        Assertions.assertFalse(whiteBacks.isSelected());
        Assertions.assertFalse(whiteBack.isSelected());
    }

    @Test
    void acceptOnlyDeckImagesUnselected() {
        String catalogPath = "./src/test/resources/svg-project/decks";
        Selectable catalog = new DecksCatalog(new File(catalogPath));
        Assertions.assertTrue(catalog.isSelected(), "Root Catalog should selected!");

        List<? extends Selectable> decks = catalog.children();
        Assertions.assertEquals(5, decks.size());

        Selectable blueDeck = searchSelectableByName(decks, "blue-deck");
        Selectable bird = searchSelectableByName(blueDeck.children(), "bird.svg");
        Selectable elemental = searchSelectableByName(blueDeck.children(), "elemental.svg");
        Selectable blueBacks = searchSelectableByName(blueDeck.children(), "backs");
        Selectable blueBack = searchSelectableByName(blueBacks.children(), "blue back.svg");
        blueDeck.unselect();
        Assertions.assertFalse(blueDeck.isSelected());
        Assertions.assertFalse(bird.isSelected());
        Assertions.assertFalse(elemental.isSelected());
        Assertions.assertFalse(blueBacks.isSelected());
        Assertions.assertFalse(blueBack.isSelected());

        Selectable redDeck = searchSelectableByName(decks, "red-deck");
        Selectable goblin = searchSelectableByName(redDeck.children(), "goblin.svg");
        Selectable dragon = searchSelectableByName(redDeck.children(), "dragon.svg");
        Selectable redBacks = searchSelectableByName(redDeck.children(), "backs");
        Selectable redBack = searchSelectableByName(redBacks.children(), "red back.svg");
        redDeck.unselect();
        Assertions.assertFalse(redDeck.isSelected());
        Assertions.assertFalse(goblin.isSelected());
        Assertions.assertFalse(dragon.isSelected());
        Assertions.assertFalse(redBacks.isSelected());
        Assertions.assertFalse(redBack.isSelected());

        Selectable greenDeck = searchSelectableByName(decks, "green-deck");
        Selectable wolf = searchSelectableByName(greenDeck.children(), "wolf.svg");
        Selectable spider = searchSelectableByName(greenDeck.children(), "spider.svg");
        Selectable greenBacks = searchSelectableByName(greenDeck.children(), "backs");
        Selectable greenBack = searchSelectableByName(greenBacks.children(), "green back.svg");
        Assertions.assertTrue(greenDeck.isSelected());
        Assertions.assertTrue(wolf.isSelected());
        Assertions.assertTrue(spider.isSelected());
        Assertions.assertTrue(greenBacks.isSelected());
        Assertions.assertTrue(greenBack.isSelected());

        Selectable blackDeck = searchSelectableByName(decks, "black-deck");
        Selectable zombie = searchSelectableByName(blackDeck.children(), "zombie.svg");
        Selectable skeleton = searchSelectableByName(blackDeck.children(), "skeleton.svg");
        Selectable blackBacks = searchSelectableByName(blackDeck.children(), "backs");
        Selectable blackBack = searchSelectableByName(blackBacks.children(), "black back.svg");
        Assertions.assertTrue(blackDeck.isSelected());
        Assertions.assertTrue(zombie.isSelected());
        Assertions.assertTrue(skeleton.isSelected());
        Assertions.assertTrue(blackBacks.isSelected());
        Assertions.assertTrue(blackBack.isSelected());

        Selectable whiteDeck = searchSelectableByName(decks, "white-deck");
        Selectable knight = searchSelectableByName(whiteDeck.children(), "knight.svg");
        Selectable whiteBacks = searchSelectableByName(whiteDeck.children(), "backs");
        Selectable whiteBack = searchSelectableByName(whiteBacks.children(), "white back.svg");
        Assertions.assertTrue(whiteDeck.isSelected());
        Assertions.assertTrue(knight.isSelected());
        Assertions.assertTrue(whiteBacks.isSelected());
        Assertions.assertTrue(whiteBack.isSelected());
    }

    @Test
    void acceptDeckUnselectedWhenAllChildImagesUnselected() {
        String catalogPath = "./src/test/resources/svg-project/decks";
        Selectable catalog = new DecksCatalog(new File(catalogPath));
        Assertions.assertTrue(catalog.isSelected(), "Root Catalog should selected by default!");

        List<? extends Selectable> decks = catalog.children();
        Assertions.assertEquals(5, decks.size());

        Selectable blueDeck = searchSelectableByName(decks, "blue-deck");
        Selectable bird = searchSelectableByName(blueDeck.children(), "bird.svg");
        Selectable elemental = searchSelectableByName(blueDeck.children(), "elemental.svg");
        Selectable blueBacks = searchSelectableByName(blueDeck.children(), "backs");
        Selectable blueBack = searchSelectableByName(blueBacks.children(), "blue back.svg");
        bird.unselect();
        elemental.unselect();
        blueBack.unselect();
        Assertions.assertFalse(blueDeck.isSelected());
        Assertions.assertFalse(bird.isSelected());
        Assertions.assertFalse(elemental.isSelected());
        Assertions.assertFalse(blueBacks.isSelected());
        Assertions.assertFalse(blueBack.isSelected());

        Selectable greenDeck = searchSelectableByName(decks, "green-deck");
        Selectable wolf = searchSelectableByName(greenDeck.children(), "wolf.svg");
        Selectable spider = searchSelectableByName(greenDeck.children(), "spider.svg");
        Selectable greenBacks = searchSelectableByName(greenDeck.children(), "backs");
        Selectable greenBack = searchSelectableByName(greenBacks.children(), "green back.svg");
        Assertions.assertTrue(greenDeck.isSelected());
        Assertions.assertTrue(wolf.isSelected());
        Assertions.assertTrue(spider.isSelected());
        Assertions.assertTrue(greenBacks.isSelected());
        Assertions.assertTrue(greenBack.isSelected());

        Selectable redDeck = searchSelectableByName(decks, "red-deck");
        Selectable goblin = searchSelectableByName(redDeck.children(), "goblin.svg");
        Selectable dragon = searchSelectableByName(redDeck.children(), "dragon.svg");
        Selectable redBacks = searchSelectableByName(redDeck.children(), "backs");
        Selectable redBack = searchSelectableByName(redBacks.children(), "red back.svg");
        Assertions.assertTrue(redDeck.isSelected());
        Assertions.assertTrue(goblin.isSelected());
        Assertions.assertTrue(dragon.isSelected());
        Assertions.assertTrue(redBacks.isSelected());
        Assertions.assertTrue(redBack.isSelected());

        Selectable blackDeck = searchSelectableByName(decks, "black-deck");
        Selectable zombie = searchSelectableByName(blackDeck.children(), "zombie.svg");
        Selectable skeleton = searchSelectableByName(blackDeck.children(), "skeleton.svg");
        Selectable blackBacks = searchSelectableByName(blackDeck.children(), "backs");
        Selectable blackBack = searchSelectableByName(blackBacks.children(), "black back.svg");
        Assertions.assertTrue(blackDeck.isSelected());
        Assertions.assertTrue(zombie.isSelected());
        Assertions.assertTrue(skeleton.isSelected());
        Assertions.assertTrue(blackBacks.isSelected());
        Assertions.assertTrue(blackBack.isSelected());

        Selectable whiteDeck = searchSelectableByName(decks, "white-deck");
        Selectable knight = searchSelectableByName(whiteDeck.children(), "knight.svg");
        Selectable whiteBacks = searchSelectableByName(whiteDeck.children(), "backs");
        Selectable whiteBack = searchSelectableByName(whiteBacks.children(), "white back.svg");
        Assertions.assertTrue(whiteDeck.isSelected());
        Assertions.assertTrue(knight.isSelected());
        Assertions.assertTrue(whiteBacks.isSelected());
        Assertions.assertTrue(whiteBack.isSelected());
    }

    @Test
    void acceptSelectedRootWhenChildImageSelected() {
        String catalogPath = "./src/test/resources/svg-project/decks";
        Selectable catalog = new DecksCatalog(new File(catalogPath));
        catalog.unselect();
        Assertions.assertFalse(catalog.isSelected(), "Root Catalog should unselected!");

        List<? extends Selectable> decks = catalog.children();
        Assertions.assertEquals(5, decks.size());

        Selectable blueDeck = searchSelectableByName(decks, "blue-deck");
        Selectable bird = searchSelectableByName(blueDeck.children(), "bird.svg");
        Selectable elemental = searchSelectableByName(blueDeck.children(), "elemental.svg");
        Selectable blueBacks = searchSelectableByName(blueDeck.children(), "backs");
        Selectable blueBack = searchSelectableByName(blueBacks.children(), "blue back.svg");
        blueBack.select();
        Assertions.assertTrue(blueDeck.isSelected());
        Assertions.assertFalse(bird.isSelected());
        Assertions.assertFalse(elemental.isSelected());
        Assertions.assertTrue(blueBacks.isSelected());
        Assertions.assertTrue(blueBack.isSelected());

        Selectable greenDeck = searchSelectableByName(decks, "green-deck");
        Selectable wolf = searchSelectableByName(greenDeck.children(), "wolf.svg");
        Selectable spider = searchSelectableByName(greenDeck.children(), "spider.svg");
        Selectable greenBacks = searchSelectableByName(greenDeck.children(), "backs");
        Selectable greenBack = searchSelectableByName(greenBacks.children(), "green back.svg");
        Assertions.assertFalse(greenDeck.isSelected());
        Assertions.assertFalse(spider.isSelected());
        Assertions.assertFalse(wolf.isSelected());
        Assertions.assertFalse(greenBacks.isSelected());
        Assertions.assertFalse(greenBack.isSelected());

        Selectable redDeck = searchSelectableByName(decks, "red-deck");
        Selectable goblin = searchSelectableByName(redDeck.children(), "goblin.svg");
        Selectable dragon = searchSelectableByName(redDeck.children(), "dragon.svg");
        Selectable redBacks = searchSelectableByName(redDeck.children(), "backs");
        Selectable redBack = searchSelectableByName(redBacks.children(), "red back.svg");
        Assertions.assertFalse(redDeck.isSelected());
        Assertions.assertFalse(goblin.isSelected());
        Assertions.assertFalse(dragon.isSelected());
        Assertions.assertFalse(redBacks.isSelected());
        Assertions.assertFalse(redBack.isSelected());

        Selectable blackDeck = searchSelectableByName(decks, "black-deck");
        Selectable zombie = searchSelectableByName(blackDeck.children(), "zombie.svg");
        Selectable skeleton = searchSelectableByName(blackDeck.children(), "skeleton.svg");
        Selectable blackBacks = searchSelectableByName(blackDeck.children(), "backs");
        Selectable blackBack = searchSelectableByName(blackBacks.children(), "black back.svg");
        Assertions.assertFalse(blackDeck.isSelected());
        Assertions.assertFalse(zombie.isSelected());
        Assertions.assertFalse(skeleton.isSelected());
        Assertions.assertFalse(blackBacks.isSelected());
        Assertions.assertFalse(blackBack.isSelected());

        Selectable whiteDeck = searchSelectableByName(decks, "white-deck");
        Selectable whiteBacks = searchSelectableByName(whiteDeck.children(), "backs");
        Selectable knight = searchSelectableByName(whiteDeck.children(), "knight.svg");
        Selectable whiteBack = searchSelectableByName(whiteBacks.children(), "white back.svg");
        Assertions.assertFalse(whiteDeck.isSelected());
        Assertions.assertFalse(knight.isSelected());
        Assertions.assertFalse(whiteBacks.isSelected());
        Assertions.assertFalse(whiteBack.isSelected());

        Assertions.assertTrue(catalog.isSelected(), "Root Catalog should selected if only one child selected!");
    }

    private Selectable searchSelectableByName(List<? extends Selectable> searchScope, String name) {
        return searchScope.stream().filter(selectable -> name.equals(selectable.getName())).findFirst()
                .orElseThrow(() -> new AssertionFailedError("Not found selection with name: " + name +", search scope: " + searchScope));
    }


}
