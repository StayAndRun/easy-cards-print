package com.karatitza.converters;

import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.converters.itext.ITextSvgToPdfConverter;

public interface ConversionFactory {

    ImageConverter create(TempFileProvider tempFileProvider);

    class InkscapeConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempFileProvider tempFileProvider) {
            return new InkscapeSvgToPdfConverter(tempFileProvider);
        }
    }

    class ITextConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempFileProvider tempFileProvider) {
            return new ITextSvgToPdfConverter(tempFileProvider);
        }
    }
}

