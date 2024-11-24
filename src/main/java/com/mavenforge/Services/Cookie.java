package com.mavenforge.Services;

import java.time.Instant;

public class Cookie {
    private final String name;
    private final String value;
    private final String domain;
    private final String path = "/";
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
        return new Cookie(name, value, domain, maxAge);
    }

    public static Cookie set(String name, String value) {
        return new Cookie(name, value);
    }

    public static Cookie set(String name, String value, String domain, Instant expiry) {
        return new Cookie(name, value, domain, expiry);
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
        return cookie;
    }
}
