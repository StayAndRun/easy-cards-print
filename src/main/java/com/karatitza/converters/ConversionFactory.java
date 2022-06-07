package com.karatitza.converters;

import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.converters.inkscape.InkscapeSvgToPngConverter;
import com.karatitza.converters.itext.ITextSvgToPdfConverter;

public interface ConversionFactory {

    ImageConverter create(TempFileProvider tempFileProvider);

    class InkscapePdfConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempFileProvider tempFileProvider) {
            return new InkscapeSvgToPdfConverter(tempFileProvider);
        }
    }

    class InkscapePngConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempFileProvider tempFileProvider) {
            return new InkscapeSvgToPngConverter(tempFileProvider);
        }
    }

    class ITextConversionFactory implements ConversionFactory {

        @Override
        public ImageConverter create(TempFileProvider tempFileProvider) {
            return new ITextSvgToPdfConverter(tempFileProvider);
        }
    }
}

