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

public class InkscapeSvgToPdfConverter implements ImageConverter {

    public static final Logger LOG = LoggerFactory.getLogger(InkscapeSvgToPdfConverter.class);

    private final TempImageFactory imageFactory;

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
        BufferedReader bufferedErrorReader = new BufferedReader(new InputStreamReader(inputErrorStream, "866"));
        bufferedErrorReader.lines().forEach(LOG::warn);
    }

    private String generateTargetPdfFileName(File back) {
        return back.getParent() + File.separator + Strings.split(back.getName(), '.')[0] + fileFormat().getExtension();
    }

    private String buildTempPdfFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + fileFormat().getExtension();
    }
}