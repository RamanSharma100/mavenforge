package com.mavenforge.Factories;

import com.mavenforge.Database.Database;

public class DatabaseFactory {
    public static Database getDatabase() {
        return new Database();
    }

}
