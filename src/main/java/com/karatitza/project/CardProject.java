package com.karatitza.project;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.converters.ConverterType;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.io.File;

public class CardProject {

    private DecksCatalog workCatalog;
    private SpotsLayout spots;

    public void selectCatalog(String path, ImageFormat format) {
        workCatalog = new DecksCatalog(new File(path), format);
    }

    public void convert(ConverterType type) {

    }

    public SpotsPreview initSpots(PageSize pageSize, SpotSize spotSize) {
        spots = new SpotsLayout(pageSize, spotSize);
        return new SpotsPreview(spots);
    }

    public void finish() {

    }
}
