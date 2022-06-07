package com.karatitza.project.layout.spots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.MessageFormat;

import static com.karatitza.project.MeasureUtils.pointsToMillimeters;
import static com.karatitza.project.MeasureUtils.round;

public class Spot {
    public final static Logger LOG = LoggerFactory.getLogger(Spot.class);

    private final float x;
    private final float y;
    private final SpotSize size;

    public Spot(float xPoints, float yPoints) {
        this.x = xPoints;
        this.y = yPoints;
        this.size = SpotSize.defaultSize();
    }

    public Spot(float x, float y, SpotSize size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public SpotSize getSize() {
        return size;
    }

    public float getCenterAlignX(float xSize) {
        return getX() - xSize / 2;
    }

    public float getCenterAlignY(float ySize) {
        return getY() - ySize / 2;
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
        if (!equalsAtFloatPoints(spot, this)) {
            LOG.warn(MessageFormat.format("Not equal at points:\n{0}\n{1}", spot, this));
        }
        return equalsAtRoundedMillimeters(spot, this);
    }

    private boolean equalsAtFloatPoints(Spot left, Spot right) {
        return left.x == right.x && left.y == right.y;
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
