package com.karatitza.converters.inkscape;

import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.converters.inkscape.console.InkscapeCommandBuilder;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

import static com.karatitza.Main.SOURCE_FILES_RELATE_PATH;
import static com.karatitza.Main.TEMP_FILES_RELATE_PATH;

public class InkscapeSvgToPlainSvgConverter implements ImageConverter {

    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPlainSvgConverter.class);

    private final TempImageFactory imageFactory;

    public InkscapeSvgToPlainSvgConverter(TempImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = imageFactory.create(sourceImage, fileFormat());
        File convertedImagePath = convertToPlainSvg(
                sourceImage.getLocation().getAbsolutePath(), targetImage.getLocation().getAbsolutePath()
        );
        return targetImage;
    }

    private File convertToPlainSvg(String sourceSvgFileName, String targetSvgFileName) {
        List<String> command = new InkscapeCommandBuilder(sourceSvgFileName)
                .enablePlainSvgOption()
                .addExportFileOption(targetSvgFileName)
                .build();
        try {
            execute(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File convertedFile = new File(targetSvgFileName);
        if (convertedFile.exists()) {
            LOG.info("Success converted file: " + convertedFile.getAbsolutePath());
        }
        return convertedFile;
    }

    private void execute(List<String> command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process process = processBuilder.start();

        InputStream inputErrorStream = process.getErrorStream();
        BufferedReader bufferedErrorReader = new BufferedReader(new InputStreamReader(inputErrorStream, "866"));
        bufferedErrorReader.lines().forEach(LOG::warn);
    }

    @Override
    public ImageFormat fileFormat() {
        return ImageFormat.SVG;
    }

    private String generateTargetPdfFileName(File back) {
        return back.getParent() + File.separator + Strings.split(back.getName(), '.')[0] + fileFormat().getExtension();
    }

    private String buildTempFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + fileFormat().getExtension();
    }

}
