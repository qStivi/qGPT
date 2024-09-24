/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.openai.OpenAiClient;
import com.qStivi.openai.OpenAiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes incoming messages and delegates them to appropriate handlers.
 */
public class MessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private static final String COMPLEX_TASK_INDICATOR = "complex";

    private final TaskManager taskManager;
    private final OpenAiClient openAiClient;

    /**
     * Constructs a MessageProcessor with the specified TaskManager and OpenAiClient.
     *
     * @param taskManager  The TaskManager to delegate complex tasks to.
     * @param openAiClient The OpenAiClient used for communicating with OpenAI services.
     */
    public MessageProcessor(TaskManager taskManager, OpenAiClient openAiClient) {
        this.taskManager = taskManager;
        this.openAiClient = openAiClient;
    }

    /**
     * Processes the message and either handles it directly or delegates to the TaskManager.
     *
     * @param input  The message to process.
     * @param userId The ID of the user who sent the message.
     * @return The response after processing the message.
     * @throws OpenAiException if an error occurs while communicating with OpenAI.
     */
    public String process(String input, String userId) throws OpenAiException {
        if (input == null || userId == null) {
            logger.warn("Input or userId is null");
            throw new IllegalArgumentException("Input and userId cannot be null");
        }

        logger.info("Processing message from user {}: {}", userId, input);

        if (requiresComplexTask(input)) {
            logger.info("Delegating to TaskManager...");
            return taskManager.handleTask(input, userId);
        }

        logger.info("Handling directly...");

        try {
            return openAiClient.sendRequest(input);
        } catch (OpenAiException e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            throw e; // Propagate the exception
        }
    }

    /**
     * Determines whether the message requires complex task handling.
     * Currently, this is a placeholder that needs to be implemented.
     *
     * @param input The input message to evaluate.
     * @return true if the message requires complex handling; false otherwise.
     */
    boolean requiresComplexTask(String input) {
        // TODO: Implement actual complexity determination logic
        logger.debug("Checking task complexity for: {}", input);
        return input.contains(COMPLEX_TASK_INDICATOR);
    }
}