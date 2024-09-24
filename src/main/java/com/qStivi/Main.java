/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.Adapters.ConsoleAdapter;
import com.qStivi.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DebugUtil.setupLogLevel();
        logger.info("Starting...");

        var core = new CoreEngine(new MessageProcessor(new TaskManager(new MemoryManager())));
        var adapter = new ConsoleAdapter();

        new Config("config.properties", ConsoleAdapter.SCANNER).getConfiguration().getProperty("openai.token");

        while (true) {
            logger.info("You: ");
            var input = adapter.receiveMessage();

            if (input.equals("exit")) {
                logger.info("Exiting...");
                break;
            }

            var userId = "1234";
            var response = core.processMessage(input, userId);
            adapter.sendMessage(response);
        }
    }
}