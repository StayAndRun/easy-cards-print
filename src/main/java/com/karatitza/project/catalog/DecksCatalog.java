package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DecksCatalog extends Selectable {
    public static final Logger LOG = LoggerFactory.getLogger(DecksCatalog.class);

    private final List<Deck> decks;

    public DecksCatalog(File rootDir) {
        super(rootDir.getName());
        List<File> decksRoots = Arrays.asList(
                Objects.requireNonNull(rootDir.listFiles(File::isDirectory), "Not found decks at dir: " + rootDir)
        );
        this.decks = new ArrayList<>(decksRoots.size());
        for (File deckRoot : decksRoots) {
            decks.add(new Deck(deckRoot));
        }
        enableChildSelectionSensitive();
    }

    private DecksCatalog(List<Deck> decks) {
        super("decks");
        this.decks = decks;
    }

    public static DecksCatalog empty() {
        return new DecksCatalog(Collections.emptyList());
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public CatalogStatistic getCatalogStatistic() {
        return new CatalogStatistic();
    }

    public DecksCatalog convert(ImageConverter converter) {
        return convert(converter, null);
    }

    public DecksCatalog convert(ImageConverter converter, Consumer<Integer> progressListener) {
        countConversionProgress(converter, progressListener);
        List<Deck> convertedDecks = decks.stream().map(deck -> deck.convert(converter)).collect(Collectors.toList());
        converter.convertBatch();
        LOG.info("Catalog conversion from {} to {} finished.", converter.inputFormat(), converter.outputFormat());
        return new DecksCatalog(convertedDecks);
    }

    private void countConversionProgress(ImageConverter converter, Consumer<Integer> progressListener) {
        if (progressListener != null) {
            converter.listenFileCreation(new Consumer<File>() {
                private int current = 0;
                private final int total = getCatalogStatistic().countImagesByFormat(converter.inputFormat());
                @Override
                public void accept(File file) {
                    current++;
                    int progressPercentage = Math.floorDiv(current * 100, total);
                    progressListener.accept(progressPercentage);
                }
            });
        }
    }

    public class CatalogStatistic {
        private final int totalImages;
        private final EnumMap<ImageFormat, Integer> imageFormatsStats = new EnumMap<>(ImageFormat.class);

        private CatalogStatistic() {
            this.totalImages = getDecks().stream().mapToInt(deck -> deck.getDeckStatistic().getTotalImages()).sum();
            for (Deck deck : getDecks()) {
                //TODO merge maps more conveniently
                Deck.DeckStatistic deckStatistic = deck.getDeckStatistic();
                for (ImageFormat format : ImageFormat.values()) {
                    if (imageFormatsStats.containsKey(format)) {
                        int count = imageFormatsStats.get(format) + deckStatistic.countImagesByFormat(format);
                        imageFormatsStats.put(format, count);
                    } else {
                        imageFormatsStats.put(format, deckStatistic.countImagesByFormat(format));
                    }
                }
            }

        }

        public int countImagesByFormat(ImageFormat format) {
            return imageFormatsStats.get(format);
        }

        public int getTotalImages() {
            return totalImages;
        }
    }

    @Override
    public String toString() {
        return "DecksCatalog{" +
                "decks=" + decks +
                '}';
    }
}
