package com.karatitza.project.layout.spots;

import com.karatitza.project.MeasureUtils;

public class SpotSize {
    private final float high;
    private final float width;
    private final float space;

    public static SpotSize millimeters(float square) {
        return new SpotSize(
                MeasureUtils.millimetersToPoints(square),
                MeasureUtils.millimetersToPoints(square),
                0
        );
    }

    public static SpotSize millimeters(float height, float width) {
        return new SpotSize(
                MeasureUtils.millimetersToPoints(height),
                MeasureUtils.millimetersToPoints(width),
                0
        );
    }

    public static SpotSize millimeters(float height, float width, float space) {
        return new SpotSize(
                MeasureUtils.millimetersToPoints(height),
                MeasureUtils.millimetersToPoints(width),
                MeasureUtils.millimetersToPoints(space)
        );
    }

    public static SpotSize points(float height, float width, float space) {
        return new SpotSize(height, width, space);
    }

    private SpotSize(float high, float width, float space) {
        this.high = high;
        this.width = width;
        this.space = space;
    }

    public float getHeight() {
        return high;
    }

    public float getWidth() {
        return width;
    }

    public float getSpace() {
        return space;
    }

    @Override
    public String toString() {
        return "SpotSize{" +
                "high=" + MeasureUtils.pointsToMillimeters(high) +
                ", width=" + MeasureUtils.pointsToMillimeters(width) +
                ", space=" + MeasureUtils.pointsToMillimeters(space) +
                '}';
    }
}
