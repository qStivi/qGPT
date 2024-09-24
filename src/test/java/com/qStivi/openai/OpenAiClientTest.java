/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.openai;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OpenAiClientTest {

    private OpenAiService mockService;
    private OpenAiClient openAiClient;

    @BeforeEach
    public void setUp() {
        // Mock the OpenAiService
        mockService = mock(OpenAiService.class);

        String modelName = "test-model";
        int maxTokens = 50;
        String defaultSystemMessage = "Test system message.";

        // Inject the mock service into OpenAiClient
        openAiClient = new OpenAiClient(mockService, modelName, maxTokens, defaultSystemMessage);
    }

    @Test
    public void testSendRequest_Success() throws OpenAiException {
        // Given
        String userInput = "Hello";
        String assistantResponse = "Hi there!";

        // Mock the service to return a valid response
        AssistantMessage assistantMessage = new AssistantMessage(assistantResponse, "assistant");
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(assistantMessage);
        List<ChatCompletionChoice> choices = Collections.singletonList(choice);

        ChatCompletionResult mockResult = new ChatCompletionResult();
        mockResult.setChoices(choices);

        when(mockService.createChatCompletion(any(ChatCompletionRequest.class))).thenReturn(mockResult);

        // When
        String response = openAiClient.sendRequest(userInput);

        // Then
        assertEquals(assistantResponse, response);

        // Verify that the messages list has been updated correctly
        List<ChatMessage> messages = openAiClient.getMessages();
        assertEquals(3, messages.size());

        assertEquals("system", messages.get(0).getRole());
        assertEquals("Test system message.", messages.get(0).getTextContent());

        assertEquals("user", messages.get(1).getRole());
        assertEquals(userInput, messages.get(1).getTextContent());

        assertEquals("assistant", messages.get(2).getRole());
        assertEquals(assistantResponse, messages.get(2).getTextContent());
    }

    @Test
    public void testSendRequest_NoChoices() {
        // Arrange
        String userInput = "Hello";
        ChatCompletionResult mockResult = new ChatCompletionResult();
        mockResult.setChoices(Collections.emptyList());
        when(mockService.createChatCompletion(any(ChatCompletionRequest.class))).thenReturn(mockResult);

        // Act & Assert
        assertThrows(OpenAiException.class, () -> openAiClient.sendRequest(userInput));
    }

    @Test
    public void testSendRequest_ServiceThrowsException() {
        // Arrange
        String userInput = "Hello";
        when(mockService.createChatCompletion(any(ChatCompletionRequest.class))).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(OpenAiException.class, () -> openAiClient.sendRequest(userInput));
    }

    @Test
    public void testResetConversation() throws OpenAiException {
        // Given
        String userInput = "Hello";
        String assistantResponse = "Hi there!";

        // Mock the service to return a valid response
        AssistantMessage assistantMessage = new AssistantMessage(assistantResponse, "assistant");
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(assistantMessage);
        List<ChatCompletionChoice> choices = Collections.singletonList(choice);

        ChatCompletionResult mockResult = new ChatCompletionResult();
        mockResult.setChoices(choices);

        when(mockService.createChatCompletion(any(ChatCompletionRequest.class))).thenReturn(mockResult);

        // Send a request to populate the messages list
        openAiClient.sendRequest(userInput);

        // When
        openAiClient.resetConversation();

        // Then
        // Verify that only the default system message remains
        List<ChatMessage> messages = openAiClient.getMessages();

        assertEquals(1, messages.size());
        assertEquals("system", messages.getFirst().getRole());
        assertEquals("Test system message.", messages.getFirst().getTextContent());
    }
}