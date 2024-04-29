package com.mavenforge.Http;

import java.util.function.BiConsumer;

import com.mavenforge.Application;

public class Route {

    public static Router get(String path, Object callback) {
        Application.router.get(path, callback);
        return Application.router;
    }

    public static Router get(String path, Class<?> controller, Object callback) {
        String controllerName = controller.getSimpleName();
        Application.router.get(path, controllerName + "@" + callback);

        return Application.router;
    }

    public static Router get(String path, BiConsumer<HTTPRequest, HTTPResponse> callback) {
        Application.router.get(path, callback);
        return Application.router;
    }

    public static Router post(String path, Object callback) {
        Application.router.post(path, callback);
        return Application.router;
    }

    public static Router post(String path, Class<?> controller, Object callback) {
        String controllerName = controller.getSimpleName();
        Application.router.post(path, controllerName + "@" + callback);

        return Application.router;
    }

    public static Router post(String path, BiConsumer<HTTPRequest, HTTPResponse> callback) {
        Application.router.post(path, callback);
        return Application.router;
    }
}
