package com.karatitza.gui.swing.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.awt.*;

//TODO temp debug implementation - move logs to file later
public class SwingAppender extends AppenderBase<ILoggingEvent> {

    private static final TextArea LOG_TEXT_AREA = new TextArea();

    static {
        LOG_TEXT_AREA.setEditable(false);
        LOG_TEXT_AREA.setBounds(10, 10, 10, 10);
        LOG_TEXT_AREA.setFont(new Font("Default", Font.PLAIN, 16));
    }

    @Override
    protected void append(ILoggingEvent event) {
        LOG_TEXT_AREA.append("%s  %s".formatted(event.getLevel(), event.getFormattedMessage()));
        LOG_TEXT_AREA.append("\n");
    }

    public static TextArea getLogsArea() {
        return LOG_TEXT_AREA;
    }

}
