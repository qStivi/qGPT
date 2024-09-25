/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code TaskManager} class handles complex tasks by managing memory retrieval
 * and performing specific actions based on user input. It also manages the reevaluation
 * process to handle additional tasks if necessary.
 */
public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
    private static final int MAX_REEVALUATIONS = 3;
    private final MemoryManager memoryManager;

    /**
     * Constructs a {@code TaskManager} with the specified {@link MemoryManager}.
     *
     * @param memoryManager The {@link MemoryManager} used for memory retrieval tasks.
     */
    public TaskManager(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    /**
     * Handles complex tasks, including memory retrieval and performing actions.
     * It also initiates the reevaluation process to handle additional tasks.
     *
     * @param input  The input describing the task.
     * @param userId The ID of the user requesting the task.
     * @return A {@code String} representing the result of the task handling.
     */
    public String handleTask(String input, String userId) {
        logger.info("Handling task for user: {}", userId);
        var result = handleTaskIteration(input, userId);

        // Now call reevaluateAndHandleMoreTasks every time
        result += "\n" + reevaluateAndHandleMoreTasks(result, userId);

        return result;
    }

    /**
     * Handles memory-related tasks by retrieving either public or private memory
     * based on the input content.
     *
     * @param input  The input describing the memory retrieval task.
     * @param userId The ID of the user requesting the memory retrieval.
     * @return A {@code String} representing the retrieved memory.
     */
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

    /**
     * Performs specific actions based on the input.
     * This method serves as a placeholder for actual action logic.
     *
     * @param action The action to be performed.
     * @return A {@code String} indicating the result of the performed action.
     */
    private String performAction(String action) {
        // Logic to perform actions (placeholder)
        logger.info("Performing action: {}", action);
        return "Performed action: " + action;
    }

    /**
     * Reevaluates after the task to determine if further actions are needed and handles them.
     *
     * @param input  The result of the initial task handling.
     * @param userId The ID of the user requesting the reevaluation.
     * @return A {@code String} representing the results of additional task handling.
     */
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

    /**
     * Handles a single task iteration without triggering another reevaluation loop.
     *
     * @param input  The input describing the task.
     * @param userId The ID of the user requesting the task.
     * @return A {@code String} representing the result of the task iteration.
     */
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