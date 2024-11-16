package com.mavenforge.Services;

import java.util.List;
import java.util.Map;

import com.mavenforge.Application;
import com.mavenforge.Database.Database;

public class Auth {
    public static boolean attempt(String email, String password) {
        Database db = Application.database;
        String databaseType = db.databaseType.toString();
        System.out.println("databaseType " + databaseType);

        switch (databaseType) {
            case "mysql":
                return attemptMySQL(email, password);
            default:
                return false;
        }

    }

    private static boolean attemptMySQL(String email, String password) {
        try {
            System.out.println("attemptMySQL");
            Application.database.mySQLDatabase.setTable("users");
            System.out.println("email " + email);

            List<Map<String, Object>> user = Application.database.mySQLDatabase.where("email", email);

            if (user.size() == 0) {
                return false;
            }

            Map<String, Object> userMap = user.get(0);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkTokenTablePresence() {
        Application.database.mySQLDatabase.setTable("remember_tokens");
        if (!Application.database.mySQLDatabase.tableExists()) {
            Application.database.mySQLDatabase.setTable("users");
            return false;
        }

        Application.database.mySQLDatabase.setTable("users");
        return true;
    }
}
