package com.karatitza.project;

import com.karatitza.project.layout.DocumentLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class PdfPagesComposer {

    public static boolean isDrawDebugMesh = false;

    protected final List<File> pdfPageFiles = new ArrayList<>();
    protected final File projectPath;

    public PdfPagesComposer(File projectPath) {
        this.projectPath = projectPath;
    }

    public abstract List<File> composeByLayout(DocumentLayout layout);

    public List<File> getFiles() {
        return new ArrayList<>(pdfPageFiles);
    }
}
