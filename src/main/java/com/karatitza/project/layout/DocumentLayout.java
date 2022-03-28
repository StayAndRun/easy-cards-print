package com.karatitza.project.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.layout.cards.CardsLayout;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentLayout implements Iterable<CardsLayout> {
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
    public Iterator<CardsLayout> iterator() {
        return cardPages.iterator();
    }
}
