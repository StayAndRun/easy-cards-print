package com.karatitza.project.layout.spots;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.MeasureUtils;
import com.karatitza.project.layout.PageFormat;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.String.format;

public class SpotsLayout implements Iterable<Spot> {
    private final int maxRowIndex;
    private final int maxColumnIndex;
    private final int rowSize;
    private final int columnSize;
    protected final Spot[][] table;
    private final PageFormat pageFormat;
    private final SpotSize spotSize;

    public SpotsLayout(PageFormat pageFormat, SpotSize spotSize) {
        this.pageFormat = pageFormat;
        this.spotSize = spotSize;
        this.rowSize = calculateSize(pageFormat.getHeight(), spotSize.getHeight(), spotSize.getSpace());
        this.maxRowIndex = rowSize - 1;
        this.columnSize = calculateSize(pageFormat.getWidth(), spotSize.getWidth(), spotSize.getSpace());
        this.maxColumnIndex = columnSize - 1;
        this.table = new Spot[columnSize][rowSize];
        calculateSpotsCoordinates(pageFormat, spotSize);
    }

    public int getMaxRowIndex() {
        return maxRowIndex;
    }

    public int getMaxColumnIndex() {
        return maxColumnIndex;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public SpotSize getSpotSize() {
        return spotSize;
    }

    public Spot getSpot(int column, int row) {
        if (column > maxColumnIndex || row > maxRowIndex) {
            throw new IndexOutOfBoundsException(format("Max column: %d, Max row: %d", maxColumnIndex, maxRowIndex));
        }
        return table[column][row];
    }

    private int calculateSize(float pageSize, float spotSize, float space) {
        int roundedPageSize = Math.round(pageSize);
        int roundedSpotSize = Math.round(spotSize);
        int roundedSpace = Math.round(space);
        return Math.floorDiv(roundedPageSize + roundedSpace, roundedSpotSize + roundedSpace);
    }

    private void calculateSpotsCoordinates(PageFormat pageFormat, SpotSize spotSize) {
        float xSpotCenterDistance = spotSize.getWidth() + spotSize.getSpace();
        float ySpotCenterDistance = spotSize.getHeight() + spotSize.getSpace();
        float xSpotsMeshCenterShift = (spotSize.getWidth() + spotSize.getSpace()) * (columnSize - 1) / 2;
        float ySpotsMeshCenterShift = (spotSize.getHeight() + spotSize.getSpace()) * (rowSize - 1) / 2;
        float xPageCenter = pageFormat.getWidth() / 2;
        float yPageCenter = pageFormat.getHeight() / 2;
        for (LayoutIterator iterator = new LayoutIterator(); iterator.hasNext(); iterator.next()) {
            int rowCursor = iterator.getRowCursor();
            int columnCursor = iterator.getColumnCursor();
            float xSpotShift = xSpotCenterDistance * columnCursor;
            float ySpotShift = ySpotCenterDistance * rowCursor;
            float xSpotCenterShift = xSpotShift - xSpotsMeshCenterShift;
            float ySpotCenterShift = ySpotShift - ySpotsMeshCenterShift;
            float xSpotAbsCoordinate = xSpotCenterShift + xPageCenter;
            float ySpotAbsCoordinate = ySpotCenterShift + yPageCenter;
            table[columnCursor][rowCursor] = new Spot(
                    MeasureUtils.round(xSpotAbsCoordinate), MeasureUtils.round(ySpotAbsCoordinate), spotSize
            );
        }
    }

    private void calculateSpotsCoordinatesBigDecimal(PageSize pageSize, SpotSize spotSize) {
        BigDecimal xSpotCenterDistance = BigDecimal.valueOf(spotSize.getWidth() + spotSize.getSpace());
        BigDecimal ySpotCenterDistance = BigDecimal.valueOf(spotSize.getHeight() + spotSize.getSpace());
        BigDecimal xSpotsMeshCenterShift = BigDecimal.valueOf(spotSize.getWidth() + spotSize.getSpace())
                .multiply(BigDecimal.valueOf(columnSize - 1))
                .divide(BigDecimal.valueOf(2), MeasureUtils.SCALE_BIG_DECIMAL, MeasureUtils.ROUNDING_MODE);
        BigDecimal ySpotsMeshCenterShift = BigDecimal.valueOf(spotSize.getHeight() + spotSize.getSpace())
                .multiply(BigDecimal.valueOf(rowSize - 1))
                .divide(BigDecimal.valueOf(2), MeasureUtils.SCALE_BIG_DECIMAL, MeasureUtils.ROUNDING_MODE);
        BigDecimal xPageCenter = BigDecimal.valueOf(pageSize.getWidth())
                .divide(BigDecimal.valueOf(2), MeasureUtils.SCALE_BIG_DECIMAL, MeasureUtils.ROUNDING_MODE);
        BigDecimal yPageCenter = BigDecimal.valueOf(pageSize.getHeight())
                .divide(BigDecimal.valueOf(2), MeasureUtils.SCALE_BIG_DECIMAL, MeasureUtils.ROUNDING_MODE);;
        for (LayoutIterator iterator = new LayoutIterator(); iterator.hasNext(); iterator.next()) {
            int rowCursor = iterator.getRowCursor();
            int columnCursor = iterator.getColumnCursor();
            BigDecimal xSpotShift = xSpotCenterDistance.multiply(BigDecimal.valueOf(columnCursor));
            BigDecimal ySpotShift = ySpotCenterDistance.multiply(BigDecimal.valueOf(rowCursor));
            BigDecimal xSpotCenterShift = xSpotShift.subtract(xSpotsMeshCenterShift);
            BigDecimal ySpotCenterShift = ySpotShift.subtract(ySpotsMeshCenterShift);
            BigDecimal xSpotAbsCoordinate = xSpotCenterShift.add(xPageCenter);
            BigDecimal ySpotAbsCoordinate = ySpotCenterShift.add(yPageCenter);
            table[columnCursor][rowCursor] = new Spot(
                    xSpotAbsCoordinate.floatValue(), ySpotAbsCoordinate.floatValue(), spotSize
            );
        }
    }

    @Override
    public Iterator<Spot> iterator() {
        return new LayoutIterator();
    }

    public LayoutIterator layoutIterator() {
        return new LayoutIterator();
    }

    public class LayoutIterator implements Iterator<Spot> {
        private int rowCursor;
        private int columnCursor;

        public int getRowCursor() {
            return rowCursor;
        }

        public int getColumnCursor() {
            return columnCursor;
        }

        @Override
        public boolean hasNext() {
            return rowCursor != rowSize && columnCursor != columnSize;
        }

        @Override
        public Spot next() {
            if (rowCursor >= rowSize && columnCursor >= columnSize) {
                throw new NoSuchElementException();
            }
            Spot current = table[columnCursor][rowCursor];
            if (rowCursor != maxRowIndex) {
                rowCursor++;
            } else {
                rowCursor = 0;
                columnCursor++;
            }
            return current;
        }
    }

}
