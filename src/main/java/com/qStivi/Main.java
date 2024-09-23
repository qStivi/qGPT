/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.Adapters.ConsoleAdapter;

public class Main {
    public static void main(String[] args) {
        var core = new CoreEngine(new MessageProcessor(new TaskManager(new MemoryManager())));
        var adapter = new ConsoleAdapter();

        while (true) {
            var input = adapter.receiveMessage();

            if (input.equals("exit")) {
                break;
            }

            var userId = "1234";
            var response = core.processMessage(input, userId);
            adapter.sendMessage(response);
        }
    }
}