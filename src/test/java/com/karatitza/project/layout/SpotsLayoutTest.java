package com.karatitza.project.layout;

import com.karatitza.project.MeasureUtils;
import com.karatitza.project.layout.spots.Spot;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.karatitza.project.MeasureUtils.millimetersToPoints;

public class SpotsLayoutTest {

    @Test
    void acceptSpots4on4() {
        SpotsLayout spots = new SpotsLayout(
                CustomPageFormat.millimeters(220, 420),
                SpotSize.millimeters(100, 50)
        );
        Assertions.assertEquals(4, spots.getColumnSize());
        Assertions.assertEquals(4, spots.getRowSize());
        Assertions.assertEquals(spot(35, 60), spots.getSpot(0,0));
        Assertions.assertEquals(spot(85, 60), spots.getSpot(1,0));
        Assertions.assertEquals(spot(135, 60), spots.getSpot(2,0));
        Assertions.assertEquals(spot(185, 60), spots.getSpot(3,0));
        Assertions.assertEquals(spot(35, 160), spots.getSpot(0,1));
        Assertions.assertEquals(spot(85, 160), spots.getSpot(1,1));
        Assertions.assertEquals(spot(135, 160), spots.getSpot(2,1));
        Assertions.assertEquals(spot(185, 160), spots.getSpot(3,1));
        Assertions.assertEquals(spot(35, 260), spots.getSpot(0,2));
        Assertions.assertEquals(spot(85, 260), spots.getSpot(1,2));
        Assertions.assertEquals(spot(135, 260), spots.getSpot(2,2));
        Assertions.assertEquals(spot(185, 260), spots.getSpot(3,2));
        Assertions.assertEquals(spot(35, 360), spots.getSpot(0,3));
        Assertions.assertEquals(spot(85, 360), spots.getSpot(1,3));
        Assertions.assertEquals(spot(135, 360), spots.getSpot(2,3));
        Assertions.assertEquals(spot(185, 360), spots.getSpot(3,3));
    }

    @Test
    void acceptSpots4on4WithSpace() {
        SpotsLayout spots = new SpotsLayout(
                CustomPageFormat.millimeters(260, 520),
                SpotSize.millimeters(100, 50, 10)
        );
        Assertions.assertEquals(4, spots.getColumnSize());
        Assertions.assertEquals(4, spots.getRowSize());
        Assertions.assertEquals(spot(40, 95), spots.getSpot(0,0));
        Assertions.assertEquals(spot(100, 95), spots.getSpot(1,0));
        Assertions.assertEquals(spot(160, 95), spots.getSpot(2,0));
        Assertions.assertEquals(spot(220, 95), spots.getSpot(3,0));
        Assertions.assertEquals(spot(40, 205), spots.getSpot(0,1));
        Assertions.assertEquals(spot(100, 205), spots.getSpot(1,1));
        Assertions.assertEquals(spot(160, 205), spots.getSpot(2,1));
        Assertions.assertEquals(spot(220, 205), spots.getSpot(3,1));
        Assertions.assertEquals(spot(40, 315), spots.getSpot(0,2));
        Assertions.assertEquals(spot(100, 315), spots.getSpot(1,2));
        Assertions.assertEquals(spot(160, 315), spots.getSpot(2,2));
        Assertions.assertEquals(spot(220, 315), spots.getSpot(3,2));
        Assertions.assertEquals(spot(40, 425), spots.getSpot(0,3));
        Assertions.assertEquals(spot(100, 425), spots.getSpot(1,3));
        Assertions.assertEquals(spot(160, 425), spots.getSpot(2,3));
        Assertions.assertEquals(spot(220, 425), spots.getSpot(3,3));
    }

    @Test
    void acceptSpots5on3() {
        SpotsLayout spots = new SpotsLayout(
                CustomPageFormat.millimeters(220, 320),
                SpotSize.millimeters(100, 40)
        );
        Assertions.assertEquals(5, spots.getColumnSize());
        Assertions.assertEquals(3, spots.getRowSize());
        Assertions.assertEquals(spot(30, 60), spots.getSpot(0,0));
        Assertions.assertEquals(spot(70, 60), spots.getSpot(1,0));
        Assertions.assertEquals(spot(110, 60), spots.getSpot(2,0));
        Assertions.assertEquals(spot(150, 60), spots.getSpot(3,0));
        Assertions.assertEquals(spot(190, 60), spots.getSpot(4,0));
        Assertions.assertEquals(spot(30, 160), spots.getSpot(0,1));
        Assertions.assertEquals(spot(70, 160), spots.getSpot(1,1));
        Assertions.assertEquals(spot(110, 160), spots.getSpot(2,1));
        Assertions.assertEquals(spot(150, 160), spots.getSpot(3,1));
        Assertions.assertEquals(spot(190, 160), spots.getSpot(4,1));
        Assertions.assertEquals(spot(30, 260), spots.getSpot(0,2));
        Assertions.assertEquals(spot(70, 260), spots.getSpot(1,2));
        Assertions.assertEquals(spot(110, 260), spots.getSpot(2,2));
        Assertions.assertEquals(spot(150, 260), spots.getSpot(3,2));
        Assertions.assertEquals(spot(190, 260), spots.getSpot(4,2));
    }

    @Test
    void acceptSpots5on3WithSpace() {
        SpotsLayout spots = new SpotsLayout(
                CustomPageFormat.millimeters(260, 400),
                SpotSize.millimeters(100, 40, 10)
        );
        Assertions.assertEquals(5, spots.getColumnSize());
        Assertions.assertEquals(3, spots.getRowSize());
        Assertions.assertEquals(spot(30, 90), spots.getSpot(0,0));
        Assertions.assertEquals(spot(80, 90), spots.getSpot(1,0));
        Assertions.assertEquals(spot(130, 90), spots.getSpot(2,0));
        Assertions.assertEquals(spot(180, 90), spots.getSpot(3,0));
        Assertions.assertEquals(spot(230, 90), spots.getSpot(4,0));
        Assertions.assertEquals(spot(30, 200), spots.getSpot(0,1));
        Assertions.assertEquals(spot(80, 200), spots.getSpot(1,1));
        Assertions.assertEquals(spot(130, 200), spots.getSpot(2,1));
        Assertions.assertEquals(spot(180, 200), spots.getSpot(3,1));
        Assertions.assertEquals(spot(230, 200), spots.getSpot(4,1));
        Assertions.assertEquals(spot(30, 310), spots.getSpot(0,2));
        Assertions.assertEquals(spot(80, 310), spots.getSpot(1,2));
        Assertions.assertEquals(spot(130, 310), spots.getSpot(2,2));
        Assertions.assertEquals(spot(180, 310), spots.getSpot(3,2));
        Assertions.assertEquals(spot(230, 310), spots.getSpot(4,2));
    }

    @Test
    void acceptMaxSpotsWithoutBorders() {
        SpotsLayout spots = new SpotsLayout(
                CustomPageFormat.millimeters(230, 430),
                SpotSize.millimeters(100, 50, 10)
        );
        Assertions.assertEquals(4, spots.getColumnSize());
        Assertions.assertEquals(4, spots.getRowSize());
        Assertions.assertEquals(spot(25, 50), spots.getSpot(0,0));
        Assertions.assertEquals(spot(205, 380), spots.getSpot(3,3));
    }

    @Test
    void acceptMeasuresConversion() {
        float points = MeasureUtils.millimetersToPoints(220);
        Assertions.assertEquals(220, MeasureUtils.pointsToMillimeters(points));

        float points2 = MeasureUtils.millimetersToPoints(35);
        Assertions.assertEquals(35, MeasureUtils.pointsToMillimeters(points2));
    }

    private Spot spot(int x, int y) {
        return new Spot(millimetersToPoints(x), millimetersToPoints(y));
    }
}
