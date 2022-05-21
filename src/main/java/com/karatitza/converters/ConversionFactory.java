package com.karatitza.converters;

import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.converters.itext.ITextSvgToPdfConverter;

public interface ConversionFactory {

    ImageConverter create(TempImageFactory tempImageFactory);

    class InkscapeConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempImageFactory tempImageFactory) {
            return new InkscapeSvgToPdfConverter(tempImageFactory);
        }
    }

    class ITextConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempImageFactory tempImageFactory) {
            return new ITextSvgToPdfConverter(tempImageFactory);
        }
    }
}

