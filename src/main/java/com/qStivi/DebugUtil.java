/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.LoggerFactory;

/**
 * The {@code DebugUtil} class provides utility methods related to debugging and logging.
 */
public class DebugUtil {

    /**
     * Sets up the log level based on whether the application is running in debug mode.
     * If the JVM is started with the debugging agent, the log level is set to DEBUG;
     * otherwise, it defaults to INFO.
     */
    public static void setupLogLevel() {
        boolean isDebugging = java.lang.management.ManagementFactory.getRuntimeMXBean()
                .getInputArguments().toString().contains("-agentlib:jdwp");
        System.setProperty("LOG_LEVEL", isDebugging ? "DEBUG" : "INFO");

        // Very important note!
        // The first log message HAS to happen AFTER the log level is set.
        var logger = LoggerFactory.getLogger(DebugUtil.class);
        logger.info("Debug mode: {}. Log level set to {}", isDebugging, System.getProperty("LOG_LEVEL"));
    }
}