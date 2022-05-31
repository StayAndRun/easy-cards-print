package com.karatitza.converters.inkscape;

import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.converters.inkscape.console.InkscapeCommandBuilder;
import com.karatitza.converters.inkscape.console.InkscapeShell;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.karatitza.Main.SOURCE_FILES_RELATE_PATH;
import static com.karatitza.Main.TEMP_FILES_RELATE_PATH;

public class InkscapeSvgToPdfConverter implements ImageConverter {

    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPdfConverter.class);

    private final TempImageFactory imageFactory;
    private final List<Image> batchImages = new ArrayList<>();
    private Consumer<File> fileCreationListener = defaultFileListener();

    public InkscapeSvgToPdfConverter(TempImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }

    @Override
    public ImageFormat fileFormat() {
        return ImageFormat.PDF;
    }

    @Override
    public Image convert(Image sourceImage) {
        Image targetImage = imageFactory.create(sourceImage, fileFormat());
        File convertedImagePath = convertFile(
                getCanonicalPath(sourceImage.getLocation()), getCanonicalPath(targetImage.getLocation())
        );
        return targetImage;
    }

    @Override
    public Image addToBatch(Image sourceImage) {
        batchImages.add(sourceImage);
        return imageFactory.create(sourceImage, fileFormat());
    }

    @Override
    public List<Image> convertBatch() {
        return executeConvertBatch(batchImages);
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

    public List<Image> executeConvertBatch(List<Image> sourceImages) {
        List<Image> convertedImages = new ArrayList<>(sourceImages.size());
        try (InkscapeShell inkscapeShell = new InkscapeShell()) {
            for (Image sourceImage : sourceImages) {
                Image tempImage = imageFactory.create(sourceImage, fileFormat());
                inkscapeShell.exportToPdfFile(sourceImage.getLocation(), tempImage.getLocation());
                convertedImages.add(tempImage);
                fileCreationListener.accept(tempImage.getLocation());
            }
        }
        return convertedImages;
    }

    @Override
    public void listenFileCreation(Consumer<File> fileCreationListener) {
        this.fileCreationListener = fileCreationListener;
    }

    private String generateTargetPdfFileName(File back) {
        return back.getParent() + File.separator + Strings.split(back.getName(), '.')[0] + fileFormat().getExtension();
    }

    private String buildTempPdfFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + fileFormat().getExtension();
    }

}