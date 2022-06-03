package com.karatitza.project.catalog;

import java.util.Optional;

public class Card {
    private final Image frontSide;
    private final Image backSide;

    public Card(Image frontSide, Image backSide) {
        this.frontSide = frontSide;
        this.backSide = backSide;
    }

    public Card(Image frontSide) {
        this.frontSide = frontSide;
        this.backSide = null;
    }

    public Image getFrontSide() {
        return frontSide;
    }

    public Optional<Image> getBackSide() {
        return Optional.ofNullable(backSide);
    }

    @Override
    public String toString() {
        return "Card{" +
                "front=" + frontSide +
                ", back=" + backSide +
                '}';
    }
}
