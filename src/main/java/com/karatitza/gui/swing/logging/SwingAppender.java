package com.karatitza.gui.swing.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import javax.swing.text.BadLocationException;

//TODO https://logback.qos.ch/manual/appenders.html#AsyncAppender
public class SwingAppender extends AppenderBase<ILoggingEvent> {

    public SwingAppender() {
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            LogPaneSingleton.getInstance().appendLog(event);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
