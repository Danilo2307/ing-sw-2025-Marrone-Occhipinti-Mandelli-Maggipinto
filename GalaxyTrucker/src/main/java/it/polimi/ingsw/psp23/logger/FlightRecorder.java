package it.polimi.ingsw.psp23.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Console Logger with colorful output and log level filtering.
 * Fully static utility class.
 */
public final class FlightRecorder {

    public enum LogLevel {
        DEBUG("🛠 DEBUG", "\u001B[90m", 1),
        INFO("ℹ️ INFO", "\u001B[34m", 2),
        WARNING("⚠️ WARNING", "\u001B[33m", 3),
        ERROR("❌ ERROR", "\u001B[31m", 4),
        NONE("🚫 NONE", "", Integer.MAX_VALUE);

        private final String label;
        private final String color;
        private final int severity;

        LogLevel(String label, String color, int severity) {
            this.label = label;
            this.color = color;
            this.severity = severity;
        }

        public String getLabel() {
            return label;
        }

        public String getColor() {
            return color;
        }

        public int getSeverity() {
            return severity;
        }

        public boolean isAllowed(LogLevel threshold) {
            return this.severity >= threshold.severity;
        }
    }

    private static LogLevel currentLevel = LogLevel.INFO;
    private static final String RESET = "\u001B[0m";

    private FlightRecorder() {
        // Prevent instantiation
    }

    public static void setLogLevel(LogLevel level) {
        currentLevel = level;
        info("Log level set to: " + level.getLabel());
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void warn(String message) {
        log(LogLevel.WARNING, message);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    private static void log(LogLevel level, String message) {
        if (!level.isAllowed(currentLevel)) return;

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(level.getColor() + "[" + level.getLabel() + "] " + timestamp + " > " + message + RESET);
    }
}
