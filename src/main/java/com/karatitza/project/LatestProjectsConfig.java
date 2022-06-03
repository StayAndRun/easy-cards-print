package com.karatitza.project;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LatestProjectsConfig {
    private static final Logger LOG = LoggerFactory.getLogger(LatestProjectsConfig.class);
    public static final String CONFIG_FILE_NAME = "./latest.json";

    private final List<String> latestProjects = new ArrayList<>();

    public static LatestProjectsConfig empty() {
        return new LatestProjectsConfig();
    }

    public static LatestProjectsConfig loadFromFile() {
        File file = new File(CONFIG_FILE_NAME);
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(file)) {
            return gson.fromJson(fileReader, LatestProjectsConfig.class);
        } catch (FileNotFoundException e) {
            LOG.info("Not found latest projects: {}", e.getMessage());
            return LatestProjectsConfig.empty();
        } catch (Exception e) {
            LOG.warn("Failed to load latest projects: ", e);
            return LatestProjectsConfig.empty();
        }
    }

    public Optional<File> saveToFile() {
        try {
            Gson gson = new Gson();
            File confFile = new File(CONFIG_FILE_NAME);
            confFile.createNewFile();
            try (FileWriter writer = new FileWriter(confFile)) {
                gson.toJson(this, writer);
            }
            return Optional.of(confFile);
        } catch (Exception e) {
            LOG.warn("Failed to save latest project: ", e);
            return Optional.empty();
        }
    }

    public void addLatestProject(File projectRoot) {
        try {
            String canonicalPath = projectRoot.getCanonicalPath();
            if (latestProjects.contains(canonicalPath)) {
                LOG.info("Latest project already saved {}", projectRoot.getName());
            } else {
                latestProjects.add(canonicalPath);
            }
        } catch (IOException e) {
            LOG.warn("Failed save latest project path: ", e);
        }
    }

    public void addLatestProject(String projectRoot) {
        addLatestProject(new File(projectRoot));
    }

    public List<File> getLatestProjects() {
        return latestProjects.stream().map(File::new).filter(File::exists).toList();
    }
}
