package com.karatitza.project;

import java.io.File;

public class CardProjectConfig {

    private final File root;

    public CardProjectConfig(File root) {
        this.root = root;
    }

    public File getRoot() {
        return root;
    }
}
