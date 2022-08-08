package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;

import java.io.File;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class Deck extends Selectable {
    public static final String BACKS_DIR_NAME = "backs";

    private final File root;
    private final List<Image> cardsImages;
    private final BacksCatalog backs;

    public Deck(File root) {
        super(root.getName());
        this.root = root;
        this.cardsImages = searchImages(root);
        this.backs = new BacksCatalog(new File(root, BACKS_DIR_NAME));
    }

    private Deck(File root, List<Image> cardsImages, List<Image> backsImages) {
        super(root.getName());
        this.root = root;
        this.cardsImages = cardsImages;
        this.backs = new BacksCatalog(new File(root, BACKS_DIR_NAME), backsImages);
    }

    public Deck selectedDeck() {
        List<Image> selectedImages = cardsImages.stream().filter(Selectable::isSelected).toList();
        List<Image> selectedBacks = backs.getBacksImages().stream().filter(Selectable::isSelected).toList();
        return new Deck(root, selectedImages, selectedBacks);
    }

    public String getName() {
        return root.getName();
    }

    public List<Card> getCards() {
        List<Image> backsImages = backs.getBacksImages();
        List<Card> cards;
        if (backsImages.isEmpty()) {
            cards = new ArrayList<>(cardsImages.size());
            for (Image cardImage : cardsImages) {
                for (int count = 1; count <= cardImage.getAmount(); count++) {
                    Card card = new Card(cardImage);
                    cards.add(card);
                }
            }
        } else {
            cards = new ArrayList<>(cardsImages.size() * backsImages.size());
            for (Image backsFile : backsImages) {
                for (Image cardImage : cardsImages) {
                    for (int count = 1; count <= cardImage.getAmount(); count++) {
                        cards.add(new Card(cardImage, backsFile));
                    }
                }
            }
        }
        return cards;
    }

    public Deck convert(ImageConverter converter) {
        List<Image> convertedCards = cardsImages.stream().map(converter::addToBatch).collect(toList());
        List<Image> convertedBacks = backs.getBacksImages().stream().map(converter::addToBatch).collect(toList());
        return new Deck(root, convertedCards, convertedBacks);
    }

    public DeckStatistic getDeckStatistic() {
        return new DeckStatistic();
    }

    @Override
    public List<? extends Selectable> children() {
        List<Selectable> children = new ArrayList<>(cardsImages.size() + 1);
        children.addAll(cardsImages);
        children.add(backs);
        return children;
    }

    private List<Image> searchImages(File imagesDir) {
        return Arrays.stream(imagesDir.listFiles())
                .filter(File::isFile)
                .flatMap(file -> Image.fromFile(file, imagesDir).stream()).collect(toList());
    }

    private Optional<File> searchBacksDir(File root) {
        return Arrays.stream(
                root.listFiles((dir, name) -> dir.isDirectory() && BACKS_DIR_NAME.equals(name))
        ).findFirst();
    }

    @Override
    public String toString() {
        return "Deck{" +
                ", cardsFiles=" + cardsImages +
                ", backsFiles=" + backs.getBacksImages() +
                '}';
    }

    public class DeckStatistic {
        private final int totalImages;
        private final int totalCards;
        private final EnumMap<ImageFormat, Integer> imageFormatsStats = new EnumMap<>(ImageFormat.class);

        public DeckStatistic() {
            List<Image> backsImages = backs.getBacksImages();
            for (ImageFormat format : ImageFormat.values()) {
                long cardImagesCount = cardsImages.stream().filter(image -> image.getFormat() == format).count();
                long backImagesCount = backsImages.stream().filter(image -> image.getFormat() == format).count();
                imageFormatsStats.put(format, Math.toIntExact(cardImagesCount + backImagesCount));
            }
            totalImages = cardsImages.size() + backsImages.size();
            totalCards = cardsImages.size();
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
