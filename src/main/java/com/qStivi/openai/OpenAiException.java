/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.openai;

/**
 * Custom exception for {@link OpenAiClient} errors.
 */
public class OpenAiException extends Exception {

    /**
     * Constructs a new {@code OpenAiException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public OpenAiException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code OpenAiException} with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public OpenAiException(String message, Throwable cause) {
        super(message, cause);
    }
}