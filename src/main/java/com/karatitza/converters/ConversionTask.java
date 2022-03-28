package com.karatitza.converters;

import com.karatitza.converters.inkscape.InkscapeSvgToPdfConverter;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import org.bouncycastle.util.Strings;

import java.io.File;

import static java.text.MessageFormat.format;

public class ConversionTask {
    public static final String TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);
    public static final String SOURCE_FILES_RELATE_PATH = format("{0}source", File.separator);

    private final String projectRoot;
    private final DecksCatalog sourceDir;
    private final ImageConverter fileConverter;

    public ConversionTask(String projectPath, ImageConverter fileConverter) {
        this.fileConverter = fileConverter;
        File sourceDir = new File(projectPath + SOURCE_FILES_RELATE_PATH);
        this.sourceDir = new DecksCatalog(new File(projectPath + SOURCE_FILES_RELATE_PATH), ImageFormat.SVG);
        this.projectRoot = sourceDir.getParent();
    }

    public void executeForSource() {
        sourceDir.convert(new InkscapeSvgToPdfConverter(new TempImageFactory(new File(projectRoot))));
    }

    public void executeForTemp() {
        DecksCatalog tempDir = new DecksCatalog(new File(projectRoot + TEMP_FILES_RELATE_PATH), ImageFormat.PDF);
        tempDir.convert(new InkscapeSvgToPdfConverter(new TempImageFactory(new File(projectRoot))));
    }

    private String generateTargetPdfFileName(File back) {
        return back.getParent()+ File.separator + Strings.split(back.getName(), '.')[0] + fileConverter.fileFormat().getExtension();
    }

    private String buildTempPdfFileName(File file) {
        return file.getParent().replace(SOURCE_FILES_RELATE_PATH, TEMP_FILES_RELATE_PATH)
                + File.separator + Strings.split(file.getName(), '.')[0] + fileConverter.fileFormat().getExtension();
    }
}
