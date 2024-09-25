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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The {@code ConfigTest} class contains unit tests for the {@link Config} class.
 * It verifies the correct behavior of configuration initialization, user prompting,
 * file creation, and exception handling.
 */
class ConfigTest {

    @TempDir
    Path tempDir;

    private Path tempConfigFile;
    private ConsoleAdapter mockConsoleAdapter;

    /**
     * Sets up the test environment by creating a temporary configuration file path
     * and mocking the {@link ConsoleAdapter}.
     *
     * @throws IOException If an I/O error occurs.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file for configuration within the temporary directory
        tempConfigFile = tempDir.resolve("test-config.properties");
        // Ensure the file does not exist to simulate non-existence
        Files.deleteIfExists(tempConfigFile);
        // Mock the ConsoleAdapter
        mockConsoleAdapter = mock(ConsoleAdapter.class);
    }

    /**
     * Cleans up the test environment by deleting the temporary configuration file after each test.
     *
     * @throws IOException If an I/O error occurs.
     */
    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temporary file after each test
        Files.deleteIfExists(tempConfigFile);
    }

    /**
     * Tests that the configuration initialization prompts for required keys when defaults are not provided.
     */
    @Test
    @DisplayName("Test Initialization with Defaults - Prompts for Required Keys")
    void testInitializationWithDefaults() {
        // Arrange
        // Simulate user input for the required key
        when(mockConsoleAdapter.receiveMessage()).thenReturn("dummy-token");

        // Act
        Config config = new Config(tempConfigFile.toString(), mockConsoleAdapter);
        Configuration configuration = config.getConfiguration();

        // Assert
        assertEquals("dummy-token", configuration.getString(ConfigKeys.OPENAI_KEY));

        // Verify that receiveMessage was called once
        verify(mockConsoleAdapter, times(1)).receiveMessage();
    }

    /**
     * Tests that the configuration initialization correctly loads user-provided input.
     */
    @Test
    @DisplayName("Test Initialization with User Input")
    void testInitializationWithUserInput() {
        // Arrange
        // Simulate user input for the required key
        when(mockConsoleAdapter.receiveMessage()).thenReturn("user-provided-token");

        // Act
        Config config = new Config(tempConfigFile.toString(), mockConsoleAdapter);
        Configuration configuration = config.getConfiguration();

        // Assert
        assertEquals("user-provided-token", configuration.getString(ConfigKeys.OPENAI_KEY));

        // Verify that receiveMessage was called once
        verify(mockConsoleAdapter, times(1)).receiveMessage();
    }

    /**
     * Tests that the configuration file is created if it does not exist during initialization.
     */
    @Test
    @DisplayName("Test Configuration File Creation")
    void testConfigFileCreation() {
        // Arrange
        assertFalse(Files.exists(tempConfigFile), "Configuration file should not exist before initialization.");
        when(mockConsoleAdapter.receiveMessage()).thenReturn("dummy-token");

        // Act
        new Config(tempConfigFile.toString(), mockConsoleAdapter);

        // Assert
        assertTrue(Files.exists(tempConfigFile), "Configuration file should be created upon initialization.");
    }

    /**
     * Tests that the configuration is correctly saved to the properties file.
     *
     * @throws IOException            If an I/O error occurs.
     * @throws ConfigurationException If a configuration error occurs.
     */
    @Test
    @DisplayName("Test Configuration Saving")
    void testConfigurationSaving() throws IOException, ConfigurationException {
        // Arrange
        when(mockConsoleAdapter.receiveMessage()).thenReturn("saved-token");

        // Act
        new Config(tempConfigFile.toString(), mockConsoleAdapter);

        // Assert
        PropertiesConfiguration savedConfig = new PropertiesConfiguration();
        try (Reader reader = Files.newBufferedReader(tempConfigFile)) {
            savedConfig.read(reader);
        }
        assertEquals("saved-token", savedConfig.getString(ConfigKeys.OPENAI_KEY));
    }

    /**
     * Tests that the user is prompted until valid input is provided.
     */
    @Test
    @DisplayName("Test Prompting Until Valid Input")
    void testPromptingUntilValidInput() {
        // Arrange
        // Simulate invalid inputs followed by a valid input
        when(mockConsoleAdapter.receiveMessage()).thenReturn("")              // First attempt: empty
                .thenReturn("   ")           // Second attempt: whitespace
                .thenReturn("valid-token");  // Third attempt: valid

        // Act
        String input = Config.promptForValidInput(ConfigKeys.OPENAI_KEY, mockConsoleAdapter);

        // Assert
        assertEquals("valid-token", input);

        // Verify that receiveMessage was called three times
        verify(mockConsoleAdapter, times(3)).receiveMessage();
    }

    /**
     * Tests that existing configuration is loaded correctly without prompting the user.
     *
     * @throws IOException           If an I/O error occurs.
     * @throws ConfigurationException If a configuration error occurs.
     */
    @Test
    @DisplayName("Test Existing Configuration Loading")
    void testExistingConfigurationLoading() throws IOException, ConfigurationException {
        // Arrange
        // Create an existing configuration file with the required key
        PropertiesConfiguration existingConfig = new PropertiesConfiguration();
        existingConfig.setProperty(ConfigKeys.OPENAI_KEY, "existing-token");
        try (Writer writer = Files.newBufferedWriter(tempConfigFile)) {
            existingConfig.write(writer);
        }

        // Act
        Config config = new Config(tempConfigFile.toString(), mockConsoleAdapter);
        Configuration configuration = config.getConfiguration();

        // Assert
        assertEquals("existing-token", configuration.getString(ConfigKeys.OPENAI_KEY));

        // Verify that receiveMessage was never called since the key exists
        verifyNoInteractions(mockConsoleAdapter);
    }

    /**
     * Tests that an exception is thrown during initialization if an I/O error occurs.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    @DisplayName("Test Exception Handling During Initialization")
    void testExceptionHandling() throws IOException {
        // Arrange
        // Create a directory instead of a file to cause an IOException
        Files.createDirectory(tempConfigFile);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new Config(tempConfigFile.toString(), mockConsoleAdapter));

        assertNotNull(exception.getCause(), "Cause of the RuntimeException should not be null.");
        assertTrue(exception.getCause() instanceof ConfigurationException || exception.getCause() instanceof IOException, "Cause should be ConfigurationException or IOException.");
    }

    /**
     * Tests the static {@link Config#promptForValidInput(String, ConsoleAdapter)} method.
     */
    @Test
    @DisplayName("Test PromptForValidInput Static Method")
    void testPromptForValidInputStaticMethod() {
        // Arrange
        when(mockConsoleAdapter.receiveMessage()).thenReturn("   ")                // First attempt: whitespace
                .thenReturn(null)                 // Second attempt: null
                .thenReturn("  valid-input ");    // Third attempt: valid input with surrounding spaces

        // Act
        String input = Config.promptForValidInput(ConfigKeys.OPENAI_KEY, mockConsoleAdapter);

        // Assert
        assertEquals("valid-input", input);

        // Verify that receiveMessage was called three times
        verify(mockConsoleAdapter, times(3)).receiveMessage();
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when initializing with a null configuration path.
     */
    @Test
    @DisplayName("Test Initialization with Null Config Path")
    void testInitializationWithNullConfigPath() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Config(null, mockConsoleAdapter));
        assertEquals("Config file path cannot be null or empty", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when initializing with an empty configuration path.
     */
    @Test
    @DisplayName("Test Initialization with Empty Config Path")
    void testInitializationWithEmptyConfigPath() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Config("", mockConsoleAdapter));
        assertEquals("Config file path cannot be null or empty", exception.getMessage());
    }
}