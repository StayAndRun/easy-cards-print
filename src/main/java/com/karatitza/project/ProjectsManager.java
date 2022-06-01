package com.karatitza.project;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProjectsManager {
    public static final Logger LOG = LoggerFactory.getLogger(ProjectsManager.class);

    private static File loadLatestRoot() {
        File file = new File("./.latest/conf.json");
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(file)) {
            LatestProjectConfiguration configuration = gson.fromJson(fileReader, LatestProjectConfiguration.class);
            File latestRoot = new File(configuration.getProjectRoot());
            LOG.info("Found latest root: {}", latestRoot);
            return latestRoot;
        } catch (FileNotFoundException e) {
            LOG.info("Not found latest project config");
            return new File("");
        } catch (IOException e) {
            LOG.warn("Failed to load latest project config");
            return new File("");
        }
    }
}
