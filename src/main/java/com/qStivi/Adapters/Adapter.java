/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.Adapters;

/**
 * The {@code Adapter} interface defines the contract for message communication.
 * Implementations of this interface are responsible for sending and receiving messages
 * through different mediums such as console, network, or other interfaces.
 */
public interface Adapter {

    /**
     * Sends a message through the adapter.
     *
     * @param message The message to be sent.
     */
    void sendMessage(String message);

    /**
     * Receives a message from the adapter.
     *
     * @return The received message as a {@code String}.
     */
    String receiveMessage();
}