/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.LoggerFactory;

public class DebugUtil {
    public static void setupLogLevel() {
        var logger = LoggerFactory.getLogger(DebugUtil.class);
        boolean isDebugging = java.lang.management.ManagementFactory.getRuntimeMXBean()
                .getInputArguments().toString().contains("-agentlib:jdwp");
        System.setProperty("LOG_LEVEL", isDebugging ? "DEBUG" : "INFO");
        logger.info("Debug mode: {}. Log level set to {}", isDebugging, System.getProperty("LOG_LEVEL"));
    }
}
