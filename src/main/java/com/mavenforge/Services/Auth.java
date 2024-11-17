package com.mavenforge.Services;

import java.util.List;
import java.util.Map;

import com.mavenforge.Application;
import com.mavenforge.Crypto.Hashing;
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

            if (Hashing.verify(password, userMap.get("password").toString())) {
                Map<String, Object> prevToken = new java.util.HashMap<String, Object>();

                if (checkTokenTablePresence()) {
                    prevToken = getPreviousToken(email);
                }

                if (!prevToken.isEmpty()) {
                    if (Long.parseLong(prevToken.get("expires_at").toString()) < System.currentTimeMillis()) {
                        Application.database.mySQLDatabase.setTable("remember_tokens");
                        Application.database.mySQLDatabase.delete("email", email);
                    } else {
                        String token = prevToken.get("token").toString();

                        Application.database.mySQLDatabase.setTable("users");

                        Object[] values = { System.currentTimeMillis() };

                        Application.database.mySQLDatabase.update("users", "last_login", values, "email = " + email);

                        return true;
                    }
                } else {
                    String token = Hashing.bin2hex(Hashing.randomBytes(32));
                    Application.database.mySQLDatabase.setTable("remember_tokens");

                    Map<String, Object> tokenData = new java.util.HashMap<String, Object>();
                    tokenData.put("user_id", userMap.get("id"));
                    tokenData.put("token", token);
                    tokenData.put("expires_at", System.currentTimeMillis() + (60 * 60 * 24 * 30));

                    Application.database.mySQLDatabase.insert(tokenData);

                    return true;
                }

                String token = Hashing.bin2hex(Hashing.randomBytes(32));
                Application.database.mySQLDatabase.setTable("remember_tokens");

                Map<String, Object> tokenData = new java.util.HashMap<String, Object>();
                tokenData.put("user_id", userMap.get("id"));
                tokenData.put("token", token);
                tokenData.put("expires_at", System.currentTimeMillis() + (60 * 60 * 24 * 30));

                Application.database.mySQLDatabase.insert(tokenData);

                return true;

            }

            return false;

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

    private static Map<String, Object> getPreviousToken(String email) {
        Application.database.mySQLDatabase.setTable("remember_tokens");
        List<Map<String, Object>> token = Application.database.mySQLDatabase.where("email", email);

        if (token.size() == 0) {
            return null;
        }

        return token.get(0);
    }
}
