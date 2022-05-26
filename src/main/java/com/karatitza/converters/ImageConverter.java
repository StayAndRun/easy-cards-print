package com.karatitza.converters;

import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;

import java.util.List;

public interface ImageConverter {

    Image convert(Image sourceImage);

    Image addToBatch(Image sourceImage);

    List<Image> convertBatch();

    ImageFormat fileFormat();
}
