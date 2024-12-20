package com.mavenforge.Services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;
import java.util.HashMap;

import com.mavenforge.Http.HTTPRequest;

public class HttpSession {
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> sessions = new ConcurrentHashMap<>();
    private static final long DEFAULT_SESSION_TIMEOUT = 30 * 60;

    public static String start() {
        String sessionId = UUID.randomUUID().toString() + "_mfg_session_" + System.currentTimeMillis();
        sessions.put(sessionId, new ConcurrentHashMap<>());
        Cookie.set("SESSION_ID", sessionId).setPath("/").setHttpOnly(true);
        return sessionId;
    }

    public static Cookie get(HTTPRequest request) {
        return Cookie.get(request, "SESSION_ID");
    }

    public static Object get(String sessionId, String key) {
        return sessions.getOrDefault(sessionId, new ConcurrentHashMap<>()).get(key);
    }

    public static void set(String sessionId, String key, Object value) {
        sessions.computeIfAbsent(sessionId, x -> new ConcurrentHashMap<>()).put(key, value);
    }

    public static void end(String sessionId) {
        Cookie.remove(sessionId);
    }

    public static boolean isActive(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public static void invalidate(String sessionId) {
        sessions.remove(sessionId);
        Cookie.delete("SESSION_ID");
    }

    public static void cleanUpExpiredSessions() {
        sessions.entrySet().removeIf(entry -> {
            Instant lastAccess = (Instant) entry.getValue().getOrDefault("LAST_ACCESS", Instant.now());
            return Instant.now().isAfter(lastAccess.plusSeconds(DEFAULT_SESSION_TIMEOUT));
        });
    }

}
