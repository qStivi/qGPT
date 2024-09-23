/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.Adapters;

import java.util.Scanner;

public class ConsoleAdapter implements Adapter {

    public final static Scanner SCANNER = new Scanner(System.in);

    @Override
    public String receiveMessage() {
        return SCANNER.nextLine();
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Bot: " + message);
    }
}
