/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.Adapters;

import java.util.Scanner;

public class ConsoleAdapter implements Adapter {

    @Override
    public String receiveMessage() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Bot: " + message);
    }
}
