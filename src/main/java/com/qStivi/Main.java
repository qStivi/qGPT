/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi;

import com.qStivi.Adapters.ConsoleAdapter;
import com.qStivi.config.Config;
import com.qStivi.config.ConfigKeys;
import com.qStivi.openai.OpenAiClient;
import com.qStivi.openai.OpenAiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code Main} class serves as the entry point for the qGPT application.
 * It initializes the configuration, sets up the core engine, and manages the main interaction loop.
 */
public class Main {

    public static final Config config = new Config("config.properties", new ConsoleAdapter());
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * The main method that starts the application.
     *
     * @param args Command-line arguments (not used).
     * @throws OpenAiException If an error occurs during message processing.
     */
    public static void main(String[] args) throws OpenAiException {
        DebugUtil.setupLogLevel();
        logger.info("Starting...");

        var core = new CoreEngine(new MessageProcessor(new TaskManager(new MemoryManager()),
                new OpenAiClient(config.getConfiguration().getString(ConfigKeys.OPENAI_KEY))));
        var adapter = new ConsoleAdapter();

        config.getConfiguration().getProperty("openai.token");

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