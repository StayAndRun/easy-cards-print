package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    public static final String BACKS_DIR_NAME = "backs";

    private final File root;
    private final ImageFormat imageFormat;
    private final List<Image> cardsFiles;
    private final List<Image> backsFiles;

    public Deck(File root, ImageFormat format) {
        this.root = root;
        this.imageFormat = format;
        this.cardsFiles = searchImages(root);
        this.backsFiles = searchBacksDir(root).map(this::searchImages).orElseGet(Collections::emptyList);
    }

    private Deck(File root, ImageFormat imageFormat, List<Image> cardsFiles, List<Image> backsFiles) {
        this.root = root;
        this.imageFormat = imageFormat;
        this.cardsFiles = cardsFiles;
        this.backsFiles = backsFiles;
    }

    public String getName() {
        return root.getName();
    }

    public List<Card> getCards() {
        if (backsFiles.isEmpty()) {
            return cardsFiles.stream().map(Card::new).collect(Collectors.toList());
        } else {
            List<Card> cards = new ArrayList<>(cardsFiles.size() * backsFiles.size());
            for (Image backsFile : backsFiles) {
                for (Image cardsFile : cardsFiles) {
                    cards.add(new Card(cardsFile, backsFile));
                }
            }
            return cards;
        }
    }

    public Deck convert(ImageConverter converter) {
        List<Image> convertedCards = cardsFiles.stream().map(converter::convert).collect(Collectors.toList());
        List<Image> convertedBacks = backsFiles.stream().map(converter::convert).collect(Collectors.toList());
        return new Deck(root, converter.fileFormat(), convertedCards, convertedBacks);
    }

    private List<Image> searchImages(File imagesDir) {
        return Arrays.stream(
                imagesDir.listFiles((dir, name) -> name.endsWith(imageFormat.getExtension()))
        ).map(file -> new Image(file, this.root, imageFormat)).collect(Collectors.toList());
    }

    private Optional<File> searchBacksDir(File root) {
        return Arrays.stream(
                root.listFiles((dir, name) -> dir.isDirectory() && BACKS_DIR_NAME.equals(name))
        ).findFirst();
    }

    @Override
    public String toString() {
        return "Deck{" +
                ", cardsFiles=" + cardsFiles +
                ", backsFiles=" + backsFiles +
                '}';
    }
}
