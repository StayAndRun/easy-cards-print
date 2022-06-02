package com.karatitza.gui.swing.panels;

import com.karatitza.project.layout.PageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SpotsLayoutPreview {
    Logger LOG = LoggerFactory.getLogger(SpotsLayoutPreview.class);

    static SpotsLayoutPreview defaultLogger() {
        return new EmptySpotsLayoutPreview();
    }

    void refresh(Integer height, Integer width, Integer space, PageFormat pageFormat);

    void refresh();

    class EmptySpotsLayoutPreview implements SpotsLayoutPreview {

        @Override
        public void refresh(Integer height, Integer width, Integer space, PageFormat pageFormat) {
            LOG.info("Refreshed spot layout: height {}, width {}, space {}, format {}", height, width, space, pageFormat);
        }

        @Override
        public void refresh() {
            LOG.info("Refreshed empty preview");
        }
    }
}
