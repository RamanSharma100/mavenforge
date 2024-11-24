package com.mavenforge.Services;

import java.util.Map;
import java.util.HashMap;

public class HttpSession {
    public static final Map<String, Map<String, Object>> sessions = new HashMap<String, Map<String, Object>>();
    public static final ThreadLocal<String> currentSessionToken = new ThreadLocal<String>();

    public static String start() {
        if (isSessionStarted()) {
            return currentSessionToken.get();
        }

        String sessionToken = java.util.UUID.randomUUID().toString();
        sessions.put(sessionToken, new HashMap<String, Object>());
        currentSessionToken.set(sessionToken);
        return sessionToken;
    }

    public static boolean isSessionStarted() {
        return currentSessionToken.get() != null;
    }

    public static void destroy() {
        if (isSessionStarted()) {
            sessions.remove(currentSessionToken.get());
            currentSessionToken.remove();
        }
    }

    public static void destroy(String sessionToken) {
        sessions.remove(sessionToken);
        if (currentSessionToken.get().equals(sessionToken)) {
            currentSessionToken.remove();
        }
    }

    public static Map<String, Object> get() {
        return sessions.get(currentSessionToken.get());
    }

    public static Map<String, Object> get(String sessionToken) {
        return sessions.get(sessionToken);
    }

    public static void setAttribute(String sessionId, String key, Object value) {
        Map<String, Object> sessionData = sessions.get(sessionId);
        if (sessionData != null) {
            sessionData.put(key, value);
        }
    }

    public static void setAttribute(String key, Object value) {
        if (isSessionStarted()) {
            Map<String, Object> sessionData = sessions.get(currentSessionToken.get());
            if (sessionData != null) {
                sessionData.put(key, value);
            }
        }
    }

    public static String getCurrentSessionToken() {
        return currentSessionToken.get();
    }

    public static void set(Map<String, Object> sessionData) {
        sessions.put(currentSessionToken.get(), sessionData);
    }

    public static boolean sessionExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

}
