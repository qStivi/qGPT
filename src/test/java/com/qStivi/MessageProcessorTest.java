/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.openai.OpenAiClient;
import com.qStivi.openai.OpenAiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The {@code MessageProcessorTest} class contains unit tests for the {@link MessageProcessor} class.
 * It verifies the correct delegation of tasks, handling of simple and complex messages,
 * and exception propagation.
 */
public class MessageProcessorTest {

    private TaskManager mockTaskManager;
    private OpenAiClient mockOpenAiClient;
    private MessageProcessor messageProcessor;

    /**
     * Sets up the test environment by mocking the {@link TaskManager} and {@link OpenAiClient},
     * and initializing the {@link MessageProcessor}.
     */
    @BeforeEach
    public void setUp() {
        mockTaskManager = mock(TaskManager.class);
        mockOpenAiClient = mock(OpenAiClient.class);
        messageProcessor = new MessageProcessor(mockTaskManager, mockOpenAiClient);
    }

    /**
     * Tests that processing a null input throws an {@link IllegalArgumentException}.
     */
    @Test
    public void testProcess_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> messageProcessor.process(null, "user123"));
    }

    /**
     * Tests that processing with a null userId throws an {@link IllegalArgumentException}.
     */
    @Test
    public void testProcess_NullUserId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> messageProcessor.process("Hello", null));
    }

    /**
     * Tests that a simple message is handled directly by the {@link OpenAiClient} without delegating to the {@link TaskManager}.
     *
     * @throws OpenAiException If an error occurs during message processing.
     */
    @Test
    public void testProcess_SimpleMessage_HandlesDirectly() throws OpenAiException {
        // Arrange
        String input = "simple message";
        String userId = "user123";
        String expectedResponse = "OpenAI response";
        when(mockOpenAiClient.sendRequest(input)).thenReturn(expectedResponse);

        // Act
        String actualResponse = messageProcessor.process(input, userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(mockTaskManager, never()).handleTask(anyString(), anyString());
        verify(mockOpenAiClient, times(1)).sendRequest(input);
    }

    /**
     * Tests that a complex message is delegated to the {@link TaskManager} and not handled directly by the {@link OpenAiClient}.
     *
     * @throws OpenAiException If an error occurs during message processing.
     */
    @Test
    public void testProcess_ComplexMessage_DelegatesToTaskManager() throws OpenAiException {
        // Arrange
        String input = "this is a complex task";
        String userId = "user123";
        String expectedResponse = "TaskManager response";
        when(mockTaskManager.handleTask(input, userId)).thenReturn(expectedResponse);

        // Act
        String actualResponse = messageProcessor.process(input, userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(mockTaskManager, times(1)).handleTask(input, userId);
        verify(mockOpenAiClient, never()).sendRequest(anyString());
    }

    /**
     * Tests that an {@link OpenAiException} thrown by the {@link OpenAiClient} is propagated.
     *
     * @throws OpenAiException If an error occurs during message processing.
     */
    @Test
    public void testProcess_OpenAiException_IsPropagated() throws OpenAiException {
        // Arrange
        String input = "simple message";
        String userId = "user123";
        when(mockOpenAiClient.sendRequest(input)).thenThrow(new OpenAiException("API error"));

        // Act & Assert
        assertThrows(OpenAiException.class, () -> messageProcessor.process(input, userId));
    }

    /**
     * Tests that {@link MessageProcessor#requiresComplexTask(String)} correctly identifies complex messages.
     */
    @Test
    public void testRequiresComplexTask_ReturnsTrueForComplexMessage() {
        // Arrange
        String input = "this is a complex task";

        // Act
        boolean result = messageProcessor.requiresComplexTask(input);

        // Assert
        assertTrue(result);
    }

    /**
     * Tests that {@link MessageProcessor#requiresComplexTask(String)} correctly identifies simple messages.
     */
    @Test
    public void testRequiresComplexTask_ReturnsFalseForSimpleMessage() {
        // Arrange
        String input = "simple message";

        // Act
        boolean result = messageProcessor.requiresComplexTask(input);

        // Assert
        assertFalse(result);
    }
}