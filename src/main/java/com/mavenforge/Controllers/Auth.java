package com.mavenforge.Controllers;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.mavenforge.Application;
import com.mavenforge.Crypto.Hashing;
import com.mavenforge.Database.Database;
import com.mavenforge.Services.Cookie;
import com.mavenforge.Services.HttpSession;
import com.mavenforge.Utils.Environment;

public class Auth extends Controller {
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

                        // String session = Cookie.get("SESSION_ID").getValue();
                        String APP_NAME = Environment.get("APP_NAME");
                        System.out.println("APP_NAME " + APP_NAME);

                        Cookie.set(APP_NAME.substring(0, 3) + "_atoken_mfg", token).setExpiry(
                                Instant.ofEpochMilli(60 * 60 * 24 * 30));
                        Cookie.set(APP_NAME.substring(0, 3) + "_usr_mfg", user.toString());

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

                    String APP_NAME = Environment.get("APP_NAME");
                    System.out.println("APP_NAME " + APP_NAME);

                    Cookie.set(APP_NAME.substring(0, 3) + "_atoken_mfg", token).setExpiry(
                            Instant.ofEpochMilli(60 * 60 * 24 * 30));
                    Cookie.set(APP_NAME.substring(0, 3) + "_usr_mfg", user.toString());

                    return true;
                }

                String token = Hashing.bin2hex(Hashing.randomBytes(32));
                Application.database.mySQLDatabase.setTable("remember_tokens");

                Map<String, Object> tokenData = new java.util.HashMap<String, Object>();
                tokenData.put("user_id", userMap.get("id"));
                tokenData.put("token", token);
                tokenData.put("expires_at", System.currentTimeMillis() + (60 * 60 * 24 * 30));

                Application.database.mySQLDatabase.insert(tokenData);

                String APP_NAME = Environment.get("APP_NAME");
                System.out.println("APP_NAME " + APP_NAME);

                Cookie.set(APP_NAME.substring(0, 3) + "_atoken_mfg", token).setExpiry(
                        Instant.ofEpochMilli(60 * 60 * 24 * 30));
                Cookie.set(APP_NAME.substring(0, 3) + "_usr_mfg", user.toString());

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
