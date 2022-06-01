package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    public static final String BACKS_DIR_NAME = "backs";

    private final File root;
    private final List<Image> cardsFiles;
    private final List<Image> backsFiles;

    public Deck(File root) {
        this.root = root;
        this.cardsFiles = searchImages(root);
        this.backsFiles = searchBacksDir(root).map(this::searchImages).orElseGet(Collections::emptyList);
    }

    private Deck(File root, List<Image> cardsFiles, List<Image> backsFiles) {
        this.root = root;
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
        List<Image> convertedCards = cardsFiles.stream().map(converter::addToBatch).collect(Collectors.toList());
        List<Image> convertedBacks = backsFiles.stream().map(converter::addToBatch).collect(Collectors.toList());
        return new Deck(root, convertedCards, convertedBacks);
    }

    public DeckStatistic getDeckStatistic() {
        return new DeckStatistic();
    }

    private List<Image> searchImages(File imagesDir) {
        return Arrays.stream(imagesDir.listFiles())
                .filter(File::isFile)
                .flatMap(file -> Image.fromFile(file, this.root).stream()).collect(Collectors.toList());
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

    public class DeckStatistic {
        private final int totalImages;
        private final int totalCards;
        private final EnumMap<ImageFormat, Integer> imageFormatsStats = new EnumMap<>(ImageFormat.class);

        public DeckStatistic() {
            for (ImageFormat format : ImageFormat.values()) {
                long cardImagesCount = cardsFiles.stream().filter(image -> image.getFormat() == format).count();
                long backImagesCount = backsFiles.stream().filter(image -> image.getFormat() == format).count();
                imageFormatsStats.put(format, Math.toIntExact(cardImagesCount + backImagesCount));
            }
            totalImages = cardsFiles.size() + backsFiles.size();
            totalCards = cardsFiles.size();
        }

        public int countImagesByFormat(ImageFormat format) {
            return imageFormatsStats.get(format);
        }

        public int getTotalImages() {
            return totalImages;
        }

        public int getTotalCards() {
            return totalCards;
        }
    }
}
