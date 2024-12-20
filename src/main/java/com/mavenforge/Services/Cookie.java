package com.mavenforge.Services;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import com.mavenforge.Http.HTTPRequest;

public class Cookie {
    private static final ConcurrentHashMap<String, Cookie> cookieStore = new ConcurrentHashMap<>();
    private String name;
    private String value;
    private String domain;
    private String path = "/";
    private Instant maxAge;
    private boolean httpOnly = true;
    private boolean secure = true;

    private Cookie(String name, String value, String domain, Instant maxAge) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.maxAge = maxAge;
    }

    private Cookie(String name, String value) {
        this(name, value, null, Instant.now().plusSeconds(60 * 60 * 24 * 30));
    }

    public Cookie setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;

        return this;
    }

    public Cookie setExpiry(Instant expiry) {
        this.maxAge = expiry;
        return this;
    }

    public Cookie setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public Cookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Cookie setPath(String path) {
        this.path = path;
        return this;
    }

    public static Cookie set(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookieStore.put(name, cookie);
        return cookie;
    }

    public static Cookie set(String name, String value, String domain, Instant expiry) {
        Cookie cookie = new Cookie(name, value, domain, expiry);
        cookieStore.put(name, cookie);
        return cookie;
    }

    public String toHeaderString() {
        StringBuilder builder = new StringBuilder(name + "=" + value + "; Path=" + path);

        if (domain != null)
            builder.append("; Domain=").append(domain);
        if (maxAge != null)
            builder.append("; Expires=").append(maxAge);
        if (secure)
            builder.append("; Secure");
        if (httpOnly)
            builder.append("; HttpOnly");

        return builder.toString();
    }

    public static Cookie delete(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setExpiry(Instant.ofEpochMilli(0));
        cookieStore.remove(name);
        return cookie;
    }

    public static boolean remove(String sessionId) {
        return cookieStore.remove(sessionId) != null;
    }

    public static Cookie get(String name) {
        return cookieStore.get(name);
    }

    public String getValue() {
        return value;
    }

    public static Cookie get(HTTPRequest request, String cookieName) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.split(";");
            for (String pair : cookiePairs) {
                String[] parts = pair.trim().split("=", 2);
                if (parts.length == 2 && parts[0].equals(cookieName)) {
                    return new Cookie(parts[0], parts[1]);
                }
            }
        }
        return null;
    }

}
