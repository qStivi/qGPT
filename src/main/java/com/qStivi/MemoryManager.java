/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code MemoryManager} class handles retrieval of both public and private memory.
 * It serves as an interface to access stored data relevant to user interactions.
 */
public class MemoryManager {

    private static final Logger logger = LoggerFactory.getLogger(MemoryManager.class);

    /**
     * Retrieves public memory based on the provided input.
     *
     * @param input The input used to fetch relevant public memory.
     * @return A {@code String} representing the retrieved public memory.
     */
    public String retrievePublicMemory(String input) {
        // Logic to fetch public memory
        logger.info("Retrieving public memory for: {}", input);
        return "Public memory for: " + input;
    }

    /**
     * Retrieves private memory for a specific user based on the provided input.
     *
     * @param userId The unique identifier of the user.
     * @param input  The input used to fetch relevant private memory.
     * @return A {@code String} representing the retrieved private memory for the user.
     */
    public String retrievePrivateMemory(String userId, String input) {
        // Logic to fetch private memory for a user
        logger.info("Retrieving private memory for user: {}", userId);
        return "Private memory for user " + userId + ": " + input;
    }
}