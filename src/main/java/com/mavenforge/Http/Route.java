package com.mavenforge.Http;

import com.mavenforge.Application;

public class Route {
    public static Router get(String path, Object callback) {
        Application.router.get(path, callback);
        return Application.router;
    }

    public static Router post(String path, Object callback) {
        Application.router.post(path, callback);
        return Application.router;
    }
}
