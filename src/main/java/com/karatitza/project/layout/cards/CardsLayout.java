package com.karatitza.project.layout.cards;

import com.karatitza.project.catalog.Card;

public class CardsLayout {

    private final ImageLayout frontLayout;
    private final ImageLayout backLayout;

    public CardsLayout(int rowSize, int columnSize) {
        this.frontLayout = ImageLayout.frontLayout(rowSize, columnSize);
        this.backLayout = ImageLayout.backLayout(rowSize, columnSize);
    }

    public ImageLayout getFrontLayout() {
        return frontLayout;
    }

    public ImageLayout getBackLayout() {
        return backLayout;
    }

    public void place(Card card) {
        frontLayout.place(card.getFrontSide());
        backLayout.place(card.getBackSide().orElse(null));
    }

    public boolean isFilled() {
        return frontLayout.isFilled();
    }

}
