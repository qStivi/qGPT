/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.config;

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

public class Config {

    private final static Logger logger = LoggerFactory.getLogger(Config.class);
    private final Configuration configuration;
    private final Path configPath;
    private final Map<String, String> requiredKeysWithDefaults;
    private final Scanner scanner;

    public Config(String configFilePath, Scanner scanner) {
        logger.debug("Initializing configuration with file path: {}", configFilePath);
        this.configPath = Paths.get(configFilePath);
        this.scanner = scanner;
        this.requiredKeysWithDefaults = initializeRequiredKeysWithDefaults();
        this.configuration = initializeConfiguration();
    }

    public static String promptForValidInput(String key, Scanner scanner) {
        logger.debug("Prompting user for input for key: {}", key);
        String value;
        do {
            logger.info("Please enter the value for '{}': ", key);
            value = scanner.nextLine();
            if (value == null || value.trim().isEmpty()) {
                logger.warn("Value cannot be empty. Please try again.");
            }
        } while (value == null || value.trim().isEmpty());
        return value.trim();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

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

    private Map<String, String> initializeRequiredKeysWithDefaults() {
        logger.debug("Initializing required keys with defaults");
        Map<String, String> map = new HashMap<>();
        map.put(ConfigKeys.OPENAI_KEY, null);
//        map.put(ConfigKeys.API_TIMEOUT, "30");
//        map.put(ConfigKeys.APP_MODE, "production");
        return Collections.unmodifiableMap(map);
    }

    private Map<String, String> promptUserForConfigValues(List<String> keys) {
        logger.debug("Prompting user for values for keys: {}", keys);
        Map<String, String> map = new HashMap<>();

        for (String key : keys) {
            String value = promptForValidInput(key, scanner);
            map.put(key, value);
        }

        return map;
    }
}