package com.karatitza.project.layout.cards;

import com.karatitza.project.catalog.Image;

public abstract class ImageLayout {
    public final int maxRowIndex;
    public final int maxColumnIndex;
    protected final Image[][] table;
    protected int currentRow = 0;
    protected int currentColumn = 0;
    protected boolean isFull;

    public static ImageLayout frontLayout(int maxRows, int maxColumns) {
        return new FrontImageLayout(maxRows, maxColumns);
    }

    public static ImageLayout backLayout(int maxRows, int maxColumns) {
        return new BackImageLayout(maxRows, maxColumns);
    }

    private ImageLayout(int maxRows, int maxColumns) {
        this.maxRowIndex = maxRows - 1;
        this.maxColumnIndex = maxColumns - 1;
        table = new Image[maxColumns][maxRows];
    }

    public Image getImage(int column, int row) {
        return table[column][row];
    }

    public boolean isFilled() {
        return isFull;
    }

    public abstract boolean place(Image image);

    static class FrontImageLayout extends ImageLayout {

        public FrontImageLayout(int rows, int columns) {
            super(rows, columns);
            currentRow = maxRowIndex;
        }

        public boolean place(Image image) {
            this.table[currentColumn][currentRow] = image;
            if (currentColumn != maxColumnIndex) {
                currentColumn++;
            } else if (currentRow != 0) {
                currentRow--;
                currentColumn = 0;
            } else {
                this.isFull = true;
                return false;
            }
            return true;
        }
    }

    static class BackImageLayout extends ImageLayout {

        public BackImageLayout(int rows, int columns) {
            super(rows, columns);
            currentRow = maxRowIndex;
            currentColumn = maxColumnIndex;
        }

        public boolean place(Image image) {
            this.table[currentColumn][currentRow] = image;
            if (currentColumn != 0) {
                currentColumn--;
            } else if (currentRow != 0) {
                currentRow--;
                currentColumn = maxColumnIndex;
            } else {
                this.isFull = true;
                return false;
            }
            return true;
        }

    }
}
