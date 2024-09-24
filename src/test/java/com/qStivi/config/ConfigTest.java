/*
 * Copyright (c) 2024 Stephan Glaue
 *
 * This source code is licensed under the MIT License found in the LICENSE file in the root directory of this source tree.
 */

package com.qStivi.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigTest {

    private Path tempConfigFile;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() throws IOException {
        tempConfigFile = Files.createTempFile("test-config", ".properties");
        Files.deleteIfExists(tempConfigFile);
        mockScanner = mock(Scanner.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempConfigFile);
    }

    @Test
    void testInitializationWithDefaults() {
        // Arrange
        when(mockScanner.nextLine()).thenReturn("dummy-token");
        Config config = new Config(tempConfigFile.toString(), mockScanner);
        Configuration configuration = config.getConfiguration();

        // Act & Assert
        assertEquals("dummy-token", configuration.getString(ConfigKeys.OPENAI_KEY));
    }

    @Test
    void testInitializationWithUserInput() {
        when(mockScanner.nextLine()).thenReturn("user-provided-token");
        Config config = new Config(tempConfigFile.toString(), mockScanner);
        Configuration configuration = config.getConfiguration();
        assertEquals("user-provided-token", configuration.getString(ConfigKeys.OPENAI_KEY));
    }

    @Test
    void testConfigFileCreation() {
        // Arrange
        assertFalse(Files.exists(tempConfigFile));
        when(mockScanner.nextLine()).thenReturn("dummy-token");

        // Act
        new Config(tempConfigFile.toString(), mockScanner);

        // Assert
        assertTrue(Files.exists(tempConfigFile));
    }

    @Test
    void testConfigurationSaving() throws IOException, ConfigurationException {
        when(mockScanner.nextLine()).thenReturn("saved-token");
        new Config(tempConfigFile.toString(), mockScanner);

        PropertiesConfiguration savedConfig = new PropertiesConfiguration();
        try (Reader reader = Files.newBufferedReader(tempConfigFile)) {
            savedConfig.read(reader);
        }
        assertEquals("saved-token", savedConfig.getString(ConfigKeys.OPENAI_KEY));
    }

    @Test
    void testPromptingUntilValidInput() {
        // Arrange
        when(mockScanner.nextLine())
                .thenReturn("")
                .thenReturn("   ")
                .thenReturn("valid-token");

        // Act
        String input = Config.promptForValidInput(ConfigKeys.OPENAI_KEY, mockScanner);

        // Assert
        assertEquals("valid-token", input);
        verify(mockScanner, times(3)).nextLine();
    }

    @Test
    void testExistingConfigurationLoading() throws IOException, ConfigurationException {
        PropertiesConfiguration existingConfig = new PropertiesConfiguration();
        existingConfig.setProperty(ConfigKeys.OPENAI_KEY, "existing-token");
        try (Writer writer = Files.newBufferedWriter(tempConfigFile)) {
            existingConfig.write(writer);
        }

        Config config = new Config(tempConfigFile.toString(), mockScanner);
        Configuration configuration = config.getConfiguration();
        assertEquals("existing-token", configuration.getString(ConfigKeys.OPENAI_KEY));
        verifyNoInteractions(mockScanner);
    }

    @Test
    void testExceptionHandling() throws IOException {
        Files.createDirectory(tempConfigFile);
        Exception exception = assertThrows(RuntimeException.class, () -> new Config(tempConfigFile.toString(), mockScanner));
        assertInstanceOf(ConfigurationException.class, exception.getCause());
    }
}