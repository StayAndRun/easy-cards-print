package com.karatitza.project.layout.spots;

import com.karatitza.project.MeasureUtils;

import java.math.BigDecimal;

import static com.karatitza.project.MeasureUtils.pointsToMillimeters;
import static com.karatitza.project.MeasureUtils.round;

public class Spot {
    private final float x;
    private final float y;

    public Spot(float xPoints, float yPoints) {
        this.x = xPoints;
        this.y = yPoints;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Spot))
            return false;
        Spot spot = (Spot) obj;
        return equalsAtRoundedMillimeters(spot, this);
    }

    private boolean equalsAtRoundedMillimeters(Spot left, Spot right) {
        return Boolean.logicalAnd(
                round(BigDecimal.valueOf(pointsToMillimeters(left.x)))
                        .equals(round(BigDecimal.valueOf(pointsToMillimeters(right.x)))),
                round(BigDecimal.valueOf(pointsToMillimeters(left.y)))
                        .equals(round(BigDecimal.valueOf(pointsToMillimeters(right.y))))
        );
    }

    @Override
    public String toString() {
        return "Spot{" +
                "x=" + x + " points" +
                "(" + pointsToMillimeters(x) + " mm)" +
                ", y=" + y + " points" +
                "(" + pointsToMillimeters(y) + " mm)" +
                '}';
    }
}
