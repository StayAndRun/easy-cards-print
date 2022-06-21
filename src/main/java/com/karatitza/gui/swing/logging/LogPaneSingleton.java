package com.karatitza.gui.swing.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class LogPaneSingleton extends JTextPane {

    private static final LogPaneSingleton INSTANCE;
    private static final int LOG_LENGTH_MAXIMUM = 50000;

    static {
        INSTANCE = new LogPaneSingleton();
    }

    private LogPaneSingleton() {
        setBounds(10, 10, 10, 10);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public static LogPaneSingleton getInstance() {
        return INSTANCE;
    }

    public void appendLog(ILoggingEvent event) throws BadLocationException {
        String logMessage = " %s  %s \n".formatted(event.getLevel(), event.getFormattedMessage());
        Document document = getDocument();
        if (document.getLength() + logMessage.length() > LOG_LENGTH_MAXIMUM) {
            document.remove(0, document.getDefaultRootElement().getElement(0).getEndOffset());
        }
        StyleContext style = StyleContext.getDefaultStyleContext();
        AttributeSet attributes = style.addAttribute(
                SimpleAttributeSet.EMPTY, StyleConstants.Foreground, convertLevelToColor(event.getLevel()));
        setCharacterAttributes(attributes, false);
        document.insertString(document.getLength(), logMessage, attributes);
    }

    private Color convertLevelToColor(Level level) {
        return switch (level.toString()) {
            case "INFO" -> Color.GREEN;
            case "DEBUG", "TRACE" -> Color.BLUE;
            case "ERROR" -> Color.RED;
            case "WARN" -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

}
