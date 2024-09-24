/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.openai.OpenAiException;

public class CoreEngine {

    private final MessageProcessor messageProcessor;

    // Constructor
    public CoreEngine(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    // Method to process incoming messages from an adapter
    public String processMessage(String input, String userId) throws OpenAiException {
        return messageProcessor.process(input, userId);
    }
}