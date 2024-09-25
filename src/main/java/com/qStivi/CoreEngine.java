/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.openai.OpenAiException;

/**
 * The {@code CoreEngine} class serves as the central component of the application,
 * orchestrating the processing of messages received from various adapters.
 */
public class CoreEngine {

    private final MessageProcessor messageProcessor;

    /**
     * Constructs a {@code CoreEngine} with the specified {@link MessageProcessor}.
     *
     * @param messageProcessor The {@link MessageProcessor} used to handle incoming messages.
     */
    public CoreEngine(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    /**
     * Processes an incoming message from a user and generates a response.
     *
     * @param input  The input message from the user.
     * @param userId The unique identifier of the user.
     * @return The processed response as a {@code String}.
     * @throws OpenAiException If an error occurs during message processing.
     */
    public String processMessage(String input, String userId) throws OpenAiException {
        return messageProcessor.process(input, userId);
    }
}