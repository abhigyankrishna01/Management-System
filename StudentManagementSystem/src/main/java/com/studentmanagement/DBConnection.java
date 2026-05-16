package com.studentmanagement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Centralized database connection factory.
 *
 * Why this class exists:
 * - Keeps connection logic in one place
 * - Makes DAO classes cleaner and reusable
 * - Loads config from a local .env file (beginner friendly)
 *
 * Config values (from .env or environment variables):
 * - DB_URL
 * - DB_USER
 * - DB_PASSWORD
 */
public final class DBConnection {

    private static final String ENV_FILE_NAME = ".env";
    private static final Object LOCK = new Object();

    // Cache config so we don't re-read the file on every operation.
    private static volatile Properties cachedConfig;

    private DBConnection() {
        // Utility class: prevent instantiation.
    }

    /**
     * Creates a JDBC connection to MySQL.
     *
     * @return active database connection
     * @throws SQLException if MySQL connection fails
     */
    public static Connection getConnection() throws SQLException {
        // MySQL driver auto-registers in modern JDBC, but Class.forName keeps it explicit for beginners.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC Driver not found. Ensure mysql-connector-j dependency is present.", e);
        }

        Properties config = getConfig();
        String url = getRequired(config, "DB_URL");
        String user = getRequired(config, "DB_USER");
        String pass = getRequired(config, "DB_PASSWORD");

        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * Loads config from:
     * 1) Environment variables
     * 2) .env file in project root (or parent folders)
     */
    private static Properties getConfig() {
        if (cachedConfig != null) {
            return cachedConfig;
        }

        synchronized (LOCK) {
            if (cachedConfig == null) {
                cachedConfig = loadFromEnvFileAndSystem();
            }
        }

        return cachedConfig;
    }

    private static Properties loadFromEnvFileAndSystem() {
        Properties props = new Properties();

        // 1) Load from .env file (if present)
        Path envPath = findEnvFile();
        if (envPath != null) {
            try {
                List<String> lines = Files.readAllLines(envPath, StandardCharsets.UTF_8);
                for (String raw : lines) {
                    String line = raw.trim();

                    // Skip comments and blank lines
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }

                    int idx = line.indexOf('=');
                    if (idx <= 0) {
                        continue; // invalid line like "ABC" without '='
                    }

                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx + 1).trim();

                    // Remove optional surrounding quotes: KEY="value"
                    if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\""))
                            || (value.startsWith("'") && value.endsWith("'")))) {
                        value = value.substring(1, value.length() - 1);
                    }

                    // Only set if not already present.
                    if (!key.isBlank() && props.getProperty(key) == null) {
                        props.setProperty(key, value);
                    }
                }
            } catch (IOException e) {
                // Non-fatal: user can still use environment variables.
                System.err.println("Warning: Could not read .env file at " + envPath + ": " + e.getMessage());
            }
        }

        // 2) Environment variables override .env file (common production pattern).
        overrideFromEnvVar(props, "DB_URL");
        overrideFromEnvVar(props, "DB_USER");
        overrideFromEnvVar(props, "DB_PASSWORD");

        return props;
    }

    private static void overrideFromEnvVar(Properties props, String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            props.setProperty(key, value);
        }
    }

    private static String getRequired(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required configuration '" + key + "'. Set it in .env or as an environment variable.");
        }
        return value;
    }

    /**
     * Tries to find a .env file starting from the current working directory and
     * walking up a few parent folders.
     */
    private static Path findEnvFile() {
        Path dir = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

        // Search current dir and a few parents (useful when running from IDE / Maven).
        for (int i = 0; i < 6 && dir != null; i++) {
            Path candidate = dir.resolve(ENV_FILE_NAME);
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate;
            }
            dir = dir.getParent();
        }

        return null;
    }
}
