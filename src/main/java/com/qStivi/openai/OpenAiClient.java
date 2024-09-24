/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.openai;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client for OpenAI's chat completion service.
 * Maintains a conversation history and allows sending messages to the service.
 */
public class OpenAiClient {

    private final OpenAiService service;
    private final List<ChatMessage> messages;
    private final String modelName;
    private final int maxTokens;
    private final String defaultSystemMessage;

    /**
     * Constructs an OpenAiClient with the specified API key and default settings.
     *
     * @param apiKey The OpenAI API key.
     */
    public OpenAiClient(String apiKey) {
        this(apiKey, Duration.ofSeconds(30), "gpt-4o-mini", 50, "You are a cute cat and will speak as such.");
    }

    /**
     * Constructs an OpenAiClient with custom settings.
     *
     * @param apiKey               The OpenAI API key.
     * @param timeout              The timeout duration for API requests.
     * @param modelName            The name of the model to use.
     * @param maxTokens            The maximum number of tokens in the response.
     * @param defaultSystemMessage The default system message to start the conversation.
     */
    public OpenAiClient(String apiKey, Duration timeout, String modelName, int maxTokens, String defaultSystemMessage) {
        this.service = new OpenAiService(apiKey, timeout);
        this.messages = new ArrayList<>();
        this.modelName = modelName;
        this.maxTokens = maxTokens;
        this.defaultSystemMessage = defaultSystemMessage;
        this.messages.add(new SystemMessage(defaultSystemMessage));
    }

    /**
     * Constructs an OpenAiClient with a provided OpenAiService.
     * Useful for testing with a mocked service.
     *
     * @param service              The OpenAiService instance.
     * @param modelName            The name of the model to use.
     * @param maxTokens            The maximum number of tokens in the response.
     * @param defaultSystemMessage The default system message to start the conversation.
     */
    public OpenAiClient(OpenAiService service, String modelName, int maxTokens, String defaultSystemMessage) {
        this.service = service;
        this.messages = new ArrayList<>();
        this.modelName = modelName;
        this.maxTokens = maxTokens;
        this.defaultSystemMessage = defaultSystemMessage;
        this.messages.add(new SystemMessage(defaultSystemMessage));
    }

    /**
     * Sends a user input to the OpenAI service and returns the assistant's response.
     *
     * @param input The user's input message.
     * @return The assistant's response.
     * @throws OpenAiException If an error occurs during the API request.
     */
    public String sendRequest(String input) throws OpenAiException {
        messages.add(new UserMessage(input));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(new ArrayList<>(messages))
                .n(1)
                .maxTokens(maxTokens)
                .build();

        try {
            ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);

            if (chatCompletion.getChoices().isEmpty()) {
                throw new OpenAiException("No response received from OpenAI service.");
            }

            ChatMessage responseMessage = chatCompletion.getChoices().getFirst().getMessage();
            messages.add(responseMessage); // Add the assistant's response to the conversation history
            return responseMessage.getTextContent();

        } catch (Exception e) {
            throw new OpenAiException("Error during OpenAI request", e);
        }
    }

    /**
     * Resets the conversation history to the default system message.
     */
    public void resetConversation() {
        messages.clear();
        messages.add(new SystemMessage(defaultSystemMessage));
    }

    /**
     * Returns a copy of the conversation messages.
     *
     * @return A list of chat messages.
     */
    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }
}