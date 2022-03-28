package com.karatitza.project;

import java.math.BigDecimal;
import java.math.MathContext;

public class MeasureUtils {

    public static final int SCALE_BIG_DECIMAL = 10;
    public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    public static float round(float value) {
        return round(BigDecimal.valueOf(value)).floatValue();
    }

    public static BigDecimal round(BigDecimal value) {
        return value.setScale(3, ROUNDING_MODE);
    }

    public static float millimetersToPoints(float value) {
        return round(inchesToPoints(millimetersToInches(value)));
    }

    public static float pointsToMillimeters(float value) {
        return round(inchesToMillimeters(pointsToInches((value))));
    }

    public static BigDecimal millimetersToPoints(BigDecimal value) {
        return round(inchesToPoints(millimetersToInches(value)));
    }

    public static BigDecimal pointsToMillimeters(BigDecimal value) {
        return round(inchesToMillimeters(pointsToInches(value)));
    }

    private static BigDecimal inchesToPoints(BigDecimal value) {
        return value.multiply(new BigDecimal(72.0F));
    }

    private static BigDecimal millimetersToInches(BigDecimal value) {
        return value.divide(new BigDecimal(25.4F), SCALE_BIG_DECIMAL, ROUNDING_MODE);
    }

    private static float millimetersToInches(float value) {
        return value / 25.4F;
    }

    private static BigDecimal inchesToMillimeters(BigDecimal value) {
        return value.multiply(new BigDecimal(25.4F));
    }

    private static BigDecimal pointsToInches(BigDecimal value) {
        return value.divide(new BigDecimal(72.0F), SCALE_BIG_DECIMAL, ROUNDING_MODE);
    }

    private static float pointsToInches(float value) {
        return value / 72.0F;
    }

    private static float inchesToMillimeters(float value) {
        return value * 25.4F;
    }

    public static float inchesToPoints(float value) {
        return value * 72.0F;
    }
}
