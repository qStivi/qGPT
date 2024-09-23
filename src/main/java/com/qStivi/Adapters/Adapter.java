/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.Adapters;

public interface Adapter {
    void sendMessage(String message);

    String receiveMessage();
}
