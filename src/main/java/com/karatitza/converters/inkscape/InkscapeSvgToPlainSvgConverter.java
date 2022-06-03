package com.karatitza.converters.inkscape;

import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempFileProvider;
import com.karatitza.converters.inkscape.console.InkscapeCommandBuilder;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.karatitza.Main.SOURCE_FILES_RELATE_PATH;
import static com.karatitza.Main.TEMP_FILES_RELATE_PATH;

public class InkscapeSvgToPlainSvgConverter extends AbstractImageConverter implements ImageConverter {
    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPlainSvgConverter.class);

    private final TempFileProvider imageFactory;
    private final List<Image> images = new ArrayList<>();

    public InkscapeSvgToPlainSvgConverter(TempFileProvider imageFactory) {
        this.imageFactory = imageFactory;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = imageFactory.create(sourceImage, outputFormat());
        if (sourceImage.getFormat() == inputFormat()) {
            convertToPlainSvg(
                    sourceImage.getLocation().getAbsolutePath(), targetImage.getLocation().getAbsolutePath());
        } else {
            LOG.warn("Not supported input format {}, conversion skipped", sourceImage.getName());
            copyImage(sourceImage, targetImage);
        }
        fileCreationListener.accept(targetImage.getLocation());
        return targetImage;
    }

    @Override
    public Image addToBatch(Image sourceImage) {
        images.add(sourceImage);
        return imageFactory.create(sourceImage, outputFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return images.stream().map(this::convert).toList();
    }

    @Override
    public ImageFormat inputFormat() {
        return ImageFormat.SVG;
    }

    @Override
    public ImageFormat outputFormat() {
        return ImageFormat.SVG;
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

    private String generateTargetPdfFileName(File back) {
        return back.getParent() + File.separator + Strings.split(back.getName(), '.')[0] + outputFormat().getExtension();
    }

    private String buildTempFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + outputFormat().getExtension();
    }

}
