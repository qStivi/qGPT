/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.config;

import com.qStivi.Adapters.ConsoleAdapter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The {@code Config} class manages the application configuration by reading from and writing to
 * a properties file. It ensures that all required configuration keys are present, prompting the
 * user for input if necessary.
 */
public class Config {

    private final static Logger logger = LoggerFactory.getLogger(Config.class);
    private final Configuration configuration;
    private final Path configPath;
    private final Map<String, String> requiredKeysWithDefaults;
    private final ConsoleAdapter consoleAdapter;

    /**
     * Constructs a {@code Config} instance with the specified configuration file path and adapter.
     *
     * @param configFilePath The path to the configuration properties file.
     * @param consoleAdapter The {@link ConsoleAdapter} used for prompting user input.
     * @throws IllegalArgumentException If the configuration file path is null or empty.
     */
    public Config(String configFilePath, ConsoleAdapter consoleAdapter) {
        logger.debug("Initializing configuration with file path: {}", configFilePath);

        // Check if the configFilePath is null or empty
        if (configFilePath == null || configFilePath.trim().isEmpty()) {
            logger.error("Config file path cannot be null");
            throw new IllegalArgumentException("Config file path cannot be null or empty");
        }

        this.configPath = Paths.get(configFilePath);
        this.consoleAdapter = consoleAdapter;
        this.requiredKeysWithDefaults = initializeRequiredKeysWithDefaults();
        this.configuration = initializeConfiguration();
    }

    /**
     * Prompts the user for valid input for a given configuration key using the provided adapter.
     * Continues prompting until a non-empty input is received.
     *
     * @param key            The configuration key for which input is required.
     * @param consoleAdapter The {@link ConsoleAdapter} used to receive user input.
     * @return The validated user input as a {@code String}.
     */
    public static String promptForValidInput(String key, ConsoleAdapter consoleAdapter) {
        logger.debug("Prompting user for input for key: {}", key);
        String value;
        do {
            logger.info("Please enter the value for '{}': ", key);
            value = consoleAdapter.receiveMessage();
            if (value == null || value.trim().isEmpty()) {
                logger.warn("Value cannot be empty. Please try again.");
            }
        } while (value == null || value.trim().isEmpty());
        return value.trim();
    }

    /**
     * Retrieves the current configuration.
     *
     * @return The {@link Configuration} object containing all configuration properties.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Initializes the configuration by reading from the properties file and ensuring all required
     * keys are present. If keys are missing, prompts the user for input or sets default values.
     *
     * @return The initialized {@link Configuration} object.
     * @throws RuntimeException If an error occurs during initialization.
     */
    private Configuration initializeConfiguration() {
        logger.debug("Initializing configuration");
        try {
            if (Files.notExists(configPath)) {
                logger.info("Configuration file does not exist. Creating new file at: {}", configPath);
                Files.createFile(configPath);
            }

            logger.info("Reading configuration from file: {}", configPath);
            PropertiesConfiguration config = new PropertiesConfiguration();
            try (Reader reader = Files.newBufferedReader(configPath)) {
                config.read(reader);
            }

            logger.debug("Checking for required keys in configuration");
            List<String> keysToPrompt = new ArrayList<>();

            for (Map.Entry<String, String> entry : requiredKeysWithDefaults.entrySet()) {
                String key = entry.getKey();
                String defaultValue = entry.getValue();
                String value = config.getString(key);

                if (value == null || value.trim().isEmpty()) {
                    logger.warn("Key '{}' is missing from configuration", key);
                    if (defaultValue != null) {
                        logger.info("Setting default value for key '{}': {}", key, defaultValue);
                        config.setProperty(key, defaultValue);
                    } else {
                        logger.info("Prompting user for value for key '{}'", key);
                        keysToPrompt.add(key);
                    }
                }
            }

            logger.info("Checking for optional keys in configuration");
            if (!keysToPrompt.isEmpty()) {
                logger.info("Prompting user for values for keys: {}", keysToPrompt);
                Map<String, String> values = promptUserForConfigValues(keysToPrompt);
                values.forEach(config::setProperty);
            }

            try (Writer writer = Files.newBufferedWriter(configPath)) {
                logger.info("Writing configuration to file: {}", configPath);
                config.write(writer);
            }

            return config;
        } catch (IOException | ConfigurationException e) {
            logger.error("Failed to initialize configuration", e);
            throw new RuntimeException("Failed to initialize configuration", e);
        }
    }

    /**
     * Initializes the map of required configuration keys along with their default values.
     *
     * @return An unmodifiable map containing required keys and their default values.
     */
    Map<String, String> initializeRequiredKeysWithDefaults() {
        logger.debug("Initializing required keys with defaults");
        Map<String, String> map = new HashMap<>();
        map.put(ConfigKeys.OPENAI_KEY, null);
        return Collections.unmodifiableMap(map);
    }

    /**
     * Prompts the user for values corresponding to the specified list of configuration keys.
     *
     * @param keys The list of configuration keys for which values are required.
     * @return A map containing the configuration keys and their corresponding user-provided values.
     */
    private Map<String, String> promptUserForConfigValues(List<String> keys) {
        logger.debug("Prompting user for values for keys: {}", keys);
        Map<String, String> map = new HashMap<>();

        for (String key : keys) {
            String value = promptForValidInput(key, consoleAdapter);
            map.put(key, value);
        }

        return map;
    }
}