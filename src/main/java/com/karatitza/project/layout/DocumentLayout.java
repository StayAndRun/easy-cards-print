package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.layout.cards.CardsLayout;
import com.karatitza.project.layout.cards.ImageLayout;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentLayout implements Iterable<DocumentLayout.PageLayout> {
    private final List<CardsLayout> cardPages = new ArrayList<>();
    private final SpotsLayout spots;

    public DocumentLayout(PageSize pageSize, SpotSize spotSize) {
        this.spots = new SpotsLayout(pageSize, spotSize);
    }

    public CardsLayout createPage() {
        CardsLayout layout = new CardsLayout(spots.getRowSize(), spots.getColumnSize());
        cardPages.add(layout);
        return layout;
    }

    public SpotsLayout getSpots() {
        return spots;
    }

    public int size() {
        return cardPages.size();
    }

    public CardsLayout get(int paperNumber) {
        return cardPages.get(paperNumber);
    }

    @Override
    public Iterator<PageLayout> iterator() {
        List<PageLayout> pageLayouts = new ArrayList<>(cardPages.size()*2);
        for (CardsLayout cardPage : cardPages) {
            pageLayouts.add(new PageLayout(cardPage.getFrontLayout(), spots));
            pageLayouts.add(new PageLayout(cardPage.getBackLayout(), spots));
        }
        return pageLayouts.iterator();
    }

    public static class PageLayout implements Iterable<AbstractMap.SimpleImmutableEntry<Spot, Image>> {

        private final ImageLayout cardsLayout;
        private final SpotsLayout spotsLayout;

        public PageLayout(ImageLayout cardsLayout, SpotsLayout spotsLayout) {
            this.cardsLayout = cardsLayout;
            this.spotsLayout = spotsLayout;
        }

        public PageSize getPageSize() {
            return spotsLayout.getPageSize();
        }

        public SpotSize getSpotSize() {
            return spotsLayout.getSpotSize();
        }

        @Override
        public Iterator<AbstractMap.SimpleImmutableEntry<Spot, Image>> iterator() {
            return new Iterator<AbstractMap.SimpleImmutableEntry<Spot, Image>>() {

                private final SpotsLayout.LayoutIterator iterator = spotsLayout.layoutIterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public AbstractMap.SimpleImmutableEntry<Spot, Image> next() {
                    Image image = cardsLayout.getImage(iterator.getColumnCursor(), iterator.getRowCursor());
                    Spot spot = iterator.next();
                    return new AbstractMap.SimpleImmutableEntry<>(spot, image);
                }
            };
        }
    }
}
