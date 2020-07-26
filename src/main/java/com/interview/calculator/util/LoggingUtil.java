package com.interview.calculator.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.interview.calculator.constants.LogLevel;
import org.slf4j.LoggerFactory;

/**
 * Util class which will have logging related functionality
 */
public class LoggingUtil {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(LoggingUtil.class);

    /**
     * Checks whether given level is supported.
     *
     * @param userLogLevel : log level specified by the user
     * @return true/false
     */
    public static boolean isSupported(String userLogLevel) {
        for (LogLevel logLevel : LogLevel.values()) {
            if (logLevel.name().equalsIgnoreCase(userLogLevel)) {
                LOGGER.debug("Log level : {} is supported", userLogLevel);
                return true;
            }
        }
        LOGGER.debug("Log level : {} is not supported", userLogLevel);
        return false;
    }

    /**
     * Setting application log level as per the parameter
     *
     * @param logLevel
     */
    public static void setApplicationLogLevel(String logLevel) {
        Level level;
        if (logLevel.equalsIgnoreCase("OFF")) {
            level = Level.OFF;
        } else if ("debug".equalsIgnoreCase(logLevel) || "fine".equalsIgnoreCase(logLevel)) {
            level = Level.DEBUG;
        } else if ("warning".equalsIgnoreCase(logLevel) || "warn".equalsIgnoreCase(logLevel)) {
            level = Level.WARN;
        } else if ("error".equalsIgnoreCase(logLevel) || "severe".equalsIgnoreCase(logLevel)) {
            level = Level.ERROR;
        } else {
            level = Level.INFO;
        }
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
        LOGGER.info("Application Log level is set to : {}", logLevel);
    }
}
