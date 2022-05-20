package com.karatitza.project.layout;

import com.karatitza.project.MeasureUtils;

public class CustomPageFormat implements PageFormat {

    private final float width;
    private final float height;

    protected CustomPageFormat(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static CustomPageFormat points(float width, float height) {
        return new CustomPageFormat(width, height);
    }

   public static CustomPageFormat millimeters(float width, float height) {
        return new CustomPageFormat(
                MeasureUtils.millimetersToPoints(width),
                MeasureUtils.millimetersToPoints(height)
        );
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
