package com.karatitza.gui.swing.worker;

import com.karatitza.project.CardProjectSnapshot;

import javax.swing.*;
import java.io.File;

public class PdfBuildWorker extends SwingWorker<File, File> {

    private final CardProjectSnapshot projectSnapshot;

    public PdfBuildWorker(CardProjectSnapshot projectSnapshot) {
        this.projectSnapshot = projectSnapshot;
    }

    @Override
    protected File doInBackground() {
        return projectSnapshot.buildFinalPdf(this::setProgress);
    }

    @Override
    protected void done() {
        this.setProgress(100);
        super.done();
    }
}
