package com.karatitza.converters.inkscape;

import com.karatitza.converters.TempFileProvider;
import com.karatitza.converters.inkscape.console.InkscapeCommandBuilder;
import com.karatitza.converters.inkscape.console.InkscapeShell;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InkscapeSvgToPdfConverter extends AbstractImageConverter {

    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPdfConverter.class);

    private final TempFileProvider imageFactory;
    private final List<Image> batchImages = new ArrayList<>();

    public InkscapeSvgToPdfConverter(TempFileProvider imageFactory) {
        this.imageFactory = imageFactory;
    }

    @Override
    public ImageFormat inputFormat() {
        return ImageFormat.SVG;
    }

    @Override
    public ImageFormat outputFormat() {
        return ImageFormat.PDF;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = imageFactory.create(sourceImage, outputFormat());
        File convertedImagePath = convertFile(
                getCanonicalPath(sourceImage.getLocation()), getCanonicalPath(targetImage.getLocation())
        );
        return targetImage;
    }

    @Override
    public Image addToBatch(Image sourceImage) {
        batchImages.add(sourceImage);
        return imageFactory.create(sourceImage, outputFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return executeConvertBatch(batchImages);
    }

    public List<Image> executeConvertBatch(List<Image> sourceImages) {
        List<Image> convertedImages = new ArrayList<>(sourceImages.size());
        try (InkscapeShell inkscapeShell = new InkscapeShell()) {
            for (Image sourceImage : sourceImages) {
                Image tempImage = imageFactory.create(sourceImage, outputFormat());
                if (sourceImage.getFormat() == inputFormat()) {
                    inkscapeShell.exportToPdfFile(sourceImage.getLocation(), tempImage.getLocation());
                    fileCreationListener.accept(tempImage.getLocation());
                } else {
                    LOG.warn("Not supported input format {}, conversion skipped", sourceImage.getName());
                    copyImage(sourceImage, tempImage);
                }
                convertedImages.add(tempImage);
            }
        }
        return convertedImages;
    }

    private File convertFile(String sourceSvgFile, String targetFileName) {
        List<String> commands = new InkscapeCommandBuilder(sourceSvgFile)
                .addExportFileOption(targetFileName)
                .addExportType("pdf")
                .addPdfVersionOption("1.5")
                .build();
        try {
            execute(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File convertedFile = new File(targetFileName);
        if (convertedFile.exists()) {
            LOG.info("Success converted file: " + convertedFile.getAbsolutePath());
        }
        return convertedFile;
    }

    private String getCanonicalPath(File sourceSvgFile) {
        try {
            return sourceSvgFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(List<String> command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process process = processBuilder.start();

        InputStream inputErrorStream = process.getErrorStream();
        try (BufferedReader bufferedErrorReader = new BufferedReader(new InputStreamReader(inputErrorStream, "866"))) {
            bufferedErrorReader.lines().forEach(LOG::warn);
        }
    }
}