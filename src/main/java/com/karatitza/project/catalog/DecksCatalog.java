package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DecksCatalog {
    private final List<Deck> decks;
    private final ImageFormat imageFormat;

    public DecksCatalog(File rootDir, ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
        List<File> decksRoots = Arrays.asList(
                Objects.requireNonNull(rootDir.listFiles(File::isDirectory), "Not found decks at dir: " + rootDir)
        );
        this.decks = new ArrayList<>(decksRoots.size());
        for (File deckRoot : decksRoots) {
            decks.add(new Deck(deckRoot, imageFormat));
        }
    }

    public DecksCatalog(List<Deck> decks, ImageFormat imageFormat) {
        this.decks = decks;
        this.imageFormat = imageFormat;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public DecksCatalog convert(ImageConverter converter) {
        if (imageFormat != ImageFormat.SVG) {
            throw new UnsupportedOperationException("Unsupported format: " + imageFormat.getExtension());
        }
        List<Deck> convertedDecks = decks.stream().map(deck -> deck.convert(converter)).collect(Collectors.toList());
        return new DecksCatalog(convertedDecks, converter.fileFormat());
    }

    @Override
    public String toString() {
        return "DecksCatalog{" +
                "decks=" + decks +
                ", imageFormat=" + imageFormat +
                '}';
    }
}
