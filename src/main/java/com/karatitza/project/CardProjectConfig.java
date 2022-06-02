package com.karatitza.project;

import com.google.gson.*;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Optional;

public class CardProjectConfig {
    private static final Logger LOG = LoggerFactory.getLogger(CardProjectConfig.class);
    public static final String CONFIG_FILE_NAME = "config.json";

    private final String root;
    private final float spotSizeX;
    private final float spotSizeY;
    private final float spotSpace;
    private final PageFormat pageFormat;

    public CardProjectConfig(File root, float spotSizeX, float spotSizeY, float spotSpace, PageFormat pageFormat) throws IOException {
        this.root = root.getCanonicalPath();
        this.spotSizeX = spotSizeX;
        this.spotSizeY = spotSizeY;
        this.spotSpace = spotSpace;
        this.pageFormat = pageFormat;
    }

    public CardProjectConfig(File root, SpotSize spotSize, PageFormat pageFormat) throws IOException {
        this(root, spotSize.getWidth(), spotSize.getHeight(), spotSize.getSpace(), pageFormat);
    }

    public CardProjectConfig(File projectRoot) throws IOException {
        this.root = projectRoot.getCanonicalPath();
        this.spotSizeX = 59;
        this.spotSizeY = 91;
        this.spotSpace = 0;
        this.pageFormat = CommonPageFormat.A4;
    }

    public static Optional<CardProjectConfig> loadFromFile(File configFile) {
        if (!configFile.exists()) {
            LOG.info("Project config not exists yet: {}", configFile);
            return Optional.empty();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PageFormat.class, new PageFormatJsonDeserializer());
        Gson gson = gsonBuilder.create();
        try (FileReader fileReader = new FileReader(configFile)) {
            return Optional.of(gson.fromJson(fileReader, CardProjectConfig.class));
        } catch (FileNotFoundException e) {
            LOG.warn("Not found project config: ", e);
            return Optional.empty();
        } catch (IOException e) {
            LOG.warn("Failed to load config: ", e);
            return Optional.empty();
        }
    }

    public Optional<File> saveToFile() {
        try {
            Gson gson = new Gson();
            File confFile = new File(root, CONFIG_FILE_NAME);
            boolean created = confFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(confFile)) {
                confFile.createNewFile();
                gson.toJson(this, writer);
            }
            return Optional.of(confFile);
        } catch (IOException e) {
            LOG.warn("Failed to save project configuration: ", e);
            return Optional.empty();
        }
    }

    public SpotSize getSpotSize() {
        return SpotSize.points(spotSizeY, spotSizeX, spotSpace);
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public File getProjectRoot() {
        return new File(root);
    }

    private static class PageFormatJsonDeserializer implements JsonDeserializer<PageFormat> {
        @Override
        public PageFormat deserialize(JsonElement json, Type type, JsonDeserializationContext context
        ) throws JsonParseException {
            String pageFormat = json.getAsString();
            return CommonPageFormat.valueOf(pageFormat);
        }
    }
}
