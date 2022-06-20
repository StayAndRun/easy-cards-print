package com.karatitza.project.catalog;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BacksCatalog extends Selectable {

    private final List<Image> backsImages;

    public BacksCatalog(File root) {
        super(root.getName());
        if (root.exists() && root.isDirectory()) {
            this.backsImages = searchImages(root);
        } else {
            this.backsImages = Collections.emptyList();
        }
    }

    public BacksCatalog(File root, List<Image> backsImages) {
        super(root.getName());
        this.backsImages = backsImages;
    }

    public List<Image> getBacksImages() {
        return backsImages;
    }

    @Override
    protected List<? extends Selectable> children() {
        return backsImages;
    }

    private List<Image> searchImages(File imagesDir) {
        return Arrays.stream(imagesDir.listFiles())
                .filter(File::isFile)
                .flatMap(file -> Image.fromFile(file, imagesDir.getParentFile()).stream())
                .collect(Collectors.toList());
    }
}
