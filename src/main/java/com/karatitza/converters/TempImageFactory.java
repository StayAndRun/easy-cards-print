package com.karatitza.converters;

import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;

import java.io.File;
import java.io.IOException;

import static java.text.MessageFormat.format;

public class TempImageFactory {

    private final File projectRoot;

    public TempImageFactory(File projectRoot) {
        this.projectRoot = projectRoot;
    }

    public Image create(Image sourceImage, ImageFormat targetFormat) {
        String sourceImageRelatePath = sourceImage.getLocation().getPath()
                .replace(sourceImage.getDeckLocation().getParent(), "");
        String targetImageRelatePath = sourceImageRelatePath
                .replace(sourceImage.getFormat().getExtension(), targetFormat.getExtension());
        String newCatalogRelatePath = format("{0}print{0}temp{0}{1}{0}", File.separator, targetFormat);
        String targetImagePath = projectRoot.getPath() + newCatalogRelatePath + targetImageRelatePath;
        String targetDeckPath = projectRoot.getPath() + newCatalogRelatePath + sourceImage.getDeckLocation().getName();
        return new Image(new File(targetImagePath), new File(targetDeckPath), targetFormat);
    }

    public File createConversionLogDir() {
        File conversionLogsDir = new File(projectRoot, File.separator + "conversion-logs");
        if (!conversionLogsDir.exists()) {
            conversionLogsDir.mkdir();
        }
        return conversionLogsDir;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\GitRepo\\EasyCardsPrint\\src\\test\\resources\\svg-project\\print\\temp");
        new File(file, File.separator + "test" + File.separator).mkdirs();
    }
}
