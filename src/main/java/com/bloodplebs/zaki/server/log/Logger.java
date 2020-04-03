package com.bloodplebs.zaki.server.log;

import com.bloodplebs.zaki.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class Logger {

    private java.util.logging.Logger LOGGER;

    public Logger(String fileName, Class caller) {
        LOGGER = java.util.logging.Logger.getLogger(caller.getName());
        setProperties(fileName);
    }

    /**
     * Custom formatter for the logger
     */
    private static class ServerFormatter extends Formatter {

        public String format(LogRecord lr) {
            return new StringBuilder(50).append(lr.getLevel())
                    .append(" :: ")
                    .append(new Date())
                    .append(" :: ")
                    .append(lr.getMessage())
                    .append(System.getProperty("line.separator"))
                    .toString();
        }
    }

    /**
     * Set some initial logger properties
     */
    private void setProperties(String fileName) {

        LOGGER.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new ServerFormatter());
        LOGGER.addHandler(ch);
        try {
            FileHandler fh = new FileHandler(Utils.USER_DIR + File.separator + "logs" + File.separator + fileName);
            fh.setFormatter(new ServerFormatter());
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            severe(e);
        }
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Logs info
     * @param msg string message
     */
    public void info(String msg) {
        LOGGER.info(msg);
    }

    /**
     * Logs info
     * @param e throwable message
     */
    public void info(Throwable e) {
        LOGGER.info(e.getMessage());
    }

    /**
     * Logs severe
     * @param msg string message
     */
    public void severe(String msg) {
        LOGGER.severe(msg);
    }

    /**
     * Logs severe
     * @param e throwable message
     */
    public void severe(Throwable e) {
        LOGGER.severe(e.getMessage());
    }

    /**
     * Logs warning
     * @param e throwable message
     */
    public void warning(Throwable e) {
        LOGGER.warning(e.getMessage());
    }

    /**
     * Logs warning
     * @param msg string message
     */
    public void warning(String msg) {
        LOGGER.severe(msg);
    }
}
