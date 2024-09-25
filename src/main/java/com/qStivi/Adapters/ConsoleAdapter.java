/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.Adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleAdapter implements Adapter {
    private static final int PASTE_TIMEOUT_MS = 500; // Adjust as needed
    private final Logger logger = LoggerFactory.getLogger(ConsoleAdapter.class);

    @Override
    public String receiveMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder messageBuilder = new StringBuilder();
        long lastInputTime = System.currentTimeMillis();

        try {
            while (true) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    if (line == null) {
                        break; // EOF detected
                    }
                    messageBuilder.append(line).append(System.lineSeparator());
                    lastInputTime = System.currentTimeMillis();
                } else {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastInputTime > PASTE_TIMEOUT_MS && !messageBuilder.isEmpty()) {
                        break; // Timeout reached, consider input complete
                    }
                    Thread.onSpinWait();
                }
            }
        } catch (IOException e) {
            logger.error("Error while receiving message", e);
            Thread.currentThread().interrupt();
            return null;
        }

        return messageBuilder.toString().trim();
    }

    @Override
    public void sendMessage(String message) {
        logger.info("Bot: {}", message);
    }
}