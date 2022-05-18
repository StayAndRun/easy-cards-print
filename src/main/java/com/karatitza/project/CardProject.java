package com.karatitza.project;

import com.itextpdf.kernel.geom.PageSize;
import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.ImageFormat;
import com.karatitza.project.compose.PdfDocumentComposer;
import com.karatitza.project.compose.SpotsPreview;
import com.karatitza.project.layout.DocumentLayout;
import com.karatitza.project.layout.LayoutComposer;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class CardProject {
    public static final String SOURCE_FILE_DIR_NAME = "source";

    private DecksCatalog workCatalog;
    private SpotsLayout spotsLayout;
    private File projectRoot;

    public void selectCatalog(File projectRoot, ImageFormat format) {
        this.projectRoot = projectRoot;
        this.workCatalog = new DecksCatalog(selectSourceDir(projectRoot), format);
        System.out.println("Selected Catalog: " + workCatalog);
    }

    public SpotsPreview defineSpots(PageSize pageSize, SpotSize spotSize) {
        this.spotsLayout = new SpotsLayout(pageSize, spotSize);
        return new SpotsPreview(spotsLayout);
    }

    public File buildFinalPdf() {
        PdfDocumentComposer pdfDocumentComposer = new PdfDocumentComposer(getCurrentProjectPath());
        LayoutComposer layoutComposer = new LayoutComposer(new DocumentLayout(spotsLayout));
        DocumentLayout composedDocumentLayout = layoutComposer.compose(getCurrentCatalog());
        return pdfDocumentComposer.compose(composedDocumentLayout);
    }

    private File getCurrentProjectPath() {
        if (projectRoot == null) {
            return new File("./src/test/resources/pdf-project");
        } else {
            return projectRoot;
        }
    }

    public DecksCatalog getCurrentCatalog() {
        if (workCatalog == null) {
            return new DecksCatalog(getCurrentProjectPath(), ImageFormat.PDF);
        } else {
            return workCatalog;
        }
    }

    private File selectSourceDir(File projectRootDir) {
        File sourceDir = Arrays.stream(Objects.requireNonNull(projectRootDir.listFiles((dir, name) -> SOURCE_FILE_DIR_NAME.equals(name))))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found source dir at project: " + projectRootDir.getName()));
        return sourceDir;
    }
}
