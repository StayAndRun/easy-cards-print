package com.karatitza.project;

public class LatestProjectConfiguration {

    private String projectRoot;

    public LatestProjectConfiguration(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    public String getProjectRoot() {
        return projectRoot;
    }

    public void setProjectRoot(String projectRoot) {
        this.projectRoot = projectRoot;
    }
}
