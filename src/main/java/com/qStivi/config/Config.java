/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Config {

    private final Configuration configuration;
    private final Path configPath;
    private final Map<String, String> requiredKeysWithDefaults;
    private final Scanner scanner;

    public Config(String configFilePath, Scanner scanner) {
        this.configPath = Paths.get(configFilePath);
        this.scanner = scanner;
        this.requiredKeysWithDefaults = initializeRequiredKeysWithDefaults();
        this.configuration = initializeConfiguration();
    }

    public static String promptForValidInput(String key, Scanner scanner) {
        String value;
        do {
            System.out.print("Please enter the value for '" + key + "': ");
            value = scanner.nextLine();
            if (value == null || value.trim().isEmpty()) {
                System.out.println("Value cannot be empty. Please try again.");
            }
        } while (value == null || value.trim().isEmpty());
        return value.trim();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private Configuration initializeConfiguration() {
        try {
            if (Files.notExists(configPath)) {
                Files.createFile(configPath);
            }

            PropertiesConfiguration config = new PropertiesConfiguration();
            try (Reader reader = Files.newBufferedReader(configPath)) {
                config.read(reader);
            }

            List<String> keysToPrompt = new ArrayList<>();

            for (Map.Entry<String, String> entry : requiredKeysWithDefaults.entrySet()) {
                String key = entry.getKey();
                String defaultValue = entry.getValue();
                String value = config.getString(key);

                if (value == null || value.trim().isEmpty()) {
                    if (defaultValue != null) {
                        config.setProperty(key, defaultValue);
                    } else {
                        keysToPrompt.add(key);
                    }
                }
            }

            if (!keysToPrompt.isEmpty()) {
                Map<String, String> values = promptUserForConfigValues(keysToPrompt);
                values.forEach(config::setProperty);
            }

            try (Writer writer = Files.newBufferedWriter(configPath)) {
                config.write(writer);
            }

            return config;
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException("Failed to initialize configuration: " + e.getMessage(), e);
        }
    }

    private Map<String, String> initializeRequiredKeysWithDefaults() {
        Map<String, String> map = new HashMap<>();
        map.put(ConfigKeys.OPENAI_TOKEN, null);
        map.put(ConfigKeys.API_TIMEOUT, "30");
        map.put(ConfigKeys.APP_MODE, "production");
        return Collections.unmodifiableMap(map);
    }

    private Map<String, String> promptUserForConfigValues(List<String> keys) {
        Map<String, String> map = new HashMap<>();

        for (String key : keys) {
            String value = promptForValidInput(key, scanner);
            map.put(key, value);
        }

        return map;
    }
}