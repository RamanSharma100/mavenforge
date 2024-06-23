package com.mavenforge.Utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    public static Dotenv env = null;

    public static String get(String key) {
        if (Environment.env == null) {
            Environment.load();
        }
        return Environment.env.get(key);
    }

    private static Dotenv load() {
        try {
            if (Environment.env != null) {
                return Environment.env;
            }
            Environment.env = Dotenv.load();
            return Environment.env;
        } catch (Exception e) {
            System.err.println("Error loading environment variables");
            return null;
        }
    }
}
