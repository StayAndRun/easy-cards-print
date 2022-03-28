package com.karatitza.converters;

import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.catalog.Image;

public interface ImageConverter {

    Image convert(Image sourceImage);

    ImageFormat fileFormat();
}
