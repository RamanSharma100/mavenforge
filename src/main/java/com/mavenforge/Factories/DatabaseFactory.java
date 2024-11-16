package com.mavenforge.Factories;

import com.mavenforge.Utils.Constants;
import com.mavenforge.Database.Database;

public class DatabaseFactory {
    public static Database getDatabase() {
        String dbType = Constants.env.get("DATABASE_TYPE");
        switch (dbType) {
            case "mysql":
                return new Database();
            case "sqlite":
                return new Database();
            default:
                return null;
        }
    }

}
