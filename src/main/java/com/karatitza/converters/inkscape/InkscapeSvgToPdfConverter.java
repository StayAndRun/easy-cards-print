package com.karatitza.converters.inkscape;

import com.karatitza.converters.ImageConverter;
import com.karatitza.converters.TempImageFactory;
import com.karatitza.converters.inkscape.console.InkscapeCommandBuilder;
import com.karatitza.project.catalog.Image;
import com.karatitza.project.catalog.ImageFormat;
import org.bouncycastle.util.Strings;

import java.io.*;

import static com.karatitza.Main.SOURCE_FILES_RELATE_PATH;
import static com.karatitza.Main.TEMP_FILES_RELATE_PATH;

public class InkscapeSvgToPdfConverter implements ImageConverter {

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
                new File(sourceImage.getLocation().getAbsolutePath()), targetImage.getLocation().getAbsolutePath()
        );
        return targetImage;
    }

    private File convertFile(File sourceSvgFile, String targetFileName) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = new InkscapeCommandBuilder(sourceSvgFile.getAbsolutePath())
                .addExportFileOption(targetFileName)
                .addExportType("pdf")
                .addPdfVersionOption("1.5")
                .build();
        try {
            execute(processBuilder, command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File convertedFile = new File(targetFileName);
        if (convertedFile.exists()) {
            System.out.println("Success converted file: " + convertedFile.getAbsolutePath());
        }
        return convertedFile;
    }

    private void execute(ProcessBuilder processBuilder, String dir) throws IOException {
        processBuilder.command("cmd.exe", "/c", dir);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "866"));
        bufferedReader.lines().forEach(System.out::println);

        InputStream inputErrorStream = process.getErrorStream();
        BufferedReader bufferedErrorReader = new BufferedReader(new InputStreamReader(inputErrorStream, "866"));
        bufferedErrorReader.lines().forEach(System.out::println);
    }

    private String generateTargetPdfFileName(File back) {
        return back.getParent()+ File.separator + Strings.split(back.getName(), '.')[0] + fileFormat().getExtension();
    }

    private String buildTempPdfFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + fileFormat().getExtension();
    }
}