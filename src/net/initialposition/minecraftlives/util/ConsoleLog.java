package net.initialposition.minecraftlives.util;

import javax.annotation.Nonnull;

public class ConsoleLog {

    private final LogLevel logLevel;

    public ConsoleLog(@Nonnull LogLevel level) {
        this.logLevel = level;
    }

    public enum LogLevel {
        VERB,
        DEBG,
        INFO,
        WARN,
        ERR
    }

    public void log(String message, LogLevel level) {
        if (level.compareTo(this.logLevel) >= 0) {
            String LOG_PREFIX = "[MinecraftLives] (" + level.name() + ") ";
            System.out.println(LOG_PREFIX + message);
        }
    }
}
