/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor {

    private final TaskManager taskManager;
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    // Constructor
    public MessageProcessor(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    // Method to process the message and either handle it directly or delegate to the TaskManager
    public String process(String input, String userId) {
        logger.info("Processing message: {}", input);
        if (requiresComplexTask(input)) {
            // Delegate complex tasks (e.g., memory retrieval, actions) to TaskManager
            logger.info("Delegating to TaskManager...");
            return taskManager.handleTask(input, userId);
        } else {
            // Handle simple message processing directly
            logger.info("Handling directly...");
            return "Simple response to: " + input;
        }
    }

    // Determines whether the message requires complex task handling
    private boolean requiresComplexTask(String input) {
        // Placeholder logic for determining task complexity
        logger.info("Checking task complexity for: {}", input);
        return input.contains("complex");
    }
}
