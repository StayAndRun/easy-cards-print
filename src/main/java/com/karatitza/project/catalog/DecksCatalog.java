package com.karatitza.project.catalog;

import com.karatitza.converters.ImageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DecksCatalog {
    public static final Logger LOG = LoggerFactory.getLogger(DecksCatalog.class);

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
        return convert(converter, null);
    }

    public ImageFormat getImageFormat() {
        return imageFormat;
    }


    public CatalogStatistic getCatalogStatistic() {
        return new CatalogStatistic();
    }

    public DecksCatalog convert(ImageConverter converter, Consumer<Integer> progressListener) {
        if (imageFormat != ImageFormat.SVG) {
            throw new UnsupportedOperationException("Unsupported format: " + imageFormat.getExtension());
        }
        countConversionProgress(converter, progressListener);
        List<Deck> convertedDecks = decks.stream().map(deck -> deck.convert(converter)).collect(Collectors.toList());
        converter.convertBatch();
        LOG.info("Catalog conversion from {} to {} finished.", getImageFormat(), converter.fileFormat());
        return new DecksCatalog(convertedDecks, converter.fileFormat());
    }

    private void countConversionProgress(ImageConverter converter, Consumer<Integer> progressListener) {
        if (progressListener != null) {
            final int total = getCatalogStatistic().getTotalImages();
            converter.listenFileCreation(new Consumer<File>() {
                private int current = 0;

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

        private CatalogStatistic() {
            this.totalImages = getDecks().stream().mapToInt(deck -> deck.getDeckStatistic().getTotalImages()).sum();
        }

        public int getTotalImages() {
            return totalImages;
        }

        public int totalImages() {
            return totalImages;
        }
    }

    @Override
    public String toString() {
        return "DecksCatalog{" +
                "decks=" + decks +
                ", imageFormat=" + imageFormat +
                '}';
    }
}
