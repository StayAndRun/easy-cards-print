package com.karatitza.converters.inkscape.console;

import java.util.ArrayList;
import java.util.List;

public class InkscapeCommandBuilder {
    private static final String DEFAULT_INKSCAPE_PATH = "inkscape";
    private static final String PDF_VERSION_OPTION = "--export-pdf-version=";
    private static final String TEXT_TO_PATH_OPTION = "--export-text-to-path=";
    private static final String EXPORT_FILENAME_OPTION = "--export-filename=";
    private static final String PDF_EXPORT_TYPE_OPTION = "--export-type=";
    private static final String PLAIN_SVG_EXPORT_OPTION = "--export-plain-svg";

    private final String inkscapePath;
    private final String targetFilePath;

    private final List<String> commands = new ArrayList<>();

    public InkscapeCommandBuilder(String inkscapePath, String targetFilePath) {
        this.inkscapePath = inkscapePath;
        this.targetFilePath = targetFilePath;
        commands.add(inkscapePath);
    }

    public InkscapeCommandBuilder(String targetFilePath) {
        this(DEFAULT_INKSCAPE_PATH, targetFilePath);
    }

    public InkscapeCommandBuilder addPdfVersionOption(String version) {
        commands.add(PDF_VERSION_OPTION + version);
        return this;
    }

    public InkscapeCommandBuilder enableTextToPathOption() {
        commands.add(TEXT_TO_PATH_OPTION);
        return this;
    }

    public InkscapeCommandBuilder addExportFileOption(String filePath) {
        commands.add(EXPORT_FILENAME_OPTION + filePath);
        return this;
    }

    public InkscapeCommandBuilder addExportType(String exportType) {
        commands.add(PDF_EXPORT_TYPE_OPTION + exportType);
        return this;
    }

    public InkscapeCommandBuilder enablePlainSvgOption() {
        commands.add(PLAIN_SVG_EXPORT_OPTION);
        return this;
    }

    public List<String> build() {
        commands.add(targetFilePath);
        return commands;
    }
}
