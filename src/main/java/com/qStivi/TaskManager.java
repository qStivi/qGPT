/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {

    private static final int MAX_REEVALUATIONS = 3;
    private final MemoryManager memoryManager;
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    // Constructor
    public TaskManager(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    // Method to handle complex tasks, including memory retrieval and performing actions
    public String handleTask(String input, String userId) {
        logger.info("Handling task for user: {}", userId);
        var result = handleTaskIteration(input, userId);

        // Now call reevaluateAndHandleMoreTasks every time
        result += "\n" + reevaluateAndHandleMoreTasks(result, userId);

        return result;
    }

    // Method to handle memory retrieval
    private String handleMemoryTask(String input, String userId) {
        logger.info("Handling memory task for user: {}", userId);
        if (input.contains("private")) {
            // Retrieve private memory for the user
            return memoryManager.retrievePrivateMemory(userId, input);
        } else {
            // Retrieve public memory
            return memoryManager.retrievePublicMemory(input);
        }
    }

    // Perform specific actions based on the input
    private String performAction(String action) {
        // Logic to perform actions (placeholder)
        logger.info("Performing action: {}", action);
        return "Performed action: " + action;
    }

    // Reevaluates after the task to see if further actions are needed
    private String reevaluateAndHandleMoreTasks(String input, String userId) {
        logger.info("Reevaluating and handling more tasks for user: {}", userId);
        StringBuilder result = new StringBuilder();
        String currentInput = input;

        for (int i = 1; i <= MAX_REEVALUATIONS; i++) {
            logger.debug("Reevaluation iteration: {}", i);

            // For demonstration, modify the input based on previous output
            String newInput = currentInput + " iteration " + i;

            // Call handleTaskIteration to process newInput without further reevaluation
            String reevaluatedResult = handleTaskIteration(newInput, userId);

            result.append(reevaluatedResult).append("\n");

            // Update currentInput for the next iteration
            currentInput = reevaluatedResult;
        }

        return result.toString();
    }

    // Handles a single task iteration without triggering another reevaluation loop
    private String handleTaskIteration(String input, String userId) {
        logger.info("Handling task iteration for user: {}", userId);
        String result;

        if (input.contains("memory")) {
            result = handleMemoryTask(input, userId);
        } else if (input.contains("action")) {
            result = performAction(input);
        } else if (input.contains("stop")) {
            return "No further tasks to handle.";
        } else {
            result = "Handled other task for input: " + input;
        }

        return result;
    }
}