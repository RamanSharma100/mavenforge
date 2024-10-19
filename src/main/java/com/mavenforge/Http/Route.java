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

    public static Router add(String path, Object callback) {
        Application.router.add(path, callback);
        return Application.router;
    }

    public static Router add(String path, Class<?> controller, Object callback) {
        String controllerName = controller.getSimpleName();
        Application.router.add(path, controllerName + "@" + callback);

        return Application.router;
    }

    public static Router add(String path, BiConsumer<HTTPRequest, HTTPResponse> callback) {
        Application.router.add(path, callback);
        return Application.router;
    }

    public static Router add(String path, Object callback, String[] allowedMethods) {
        Application.router.add(path, callback, allowedMethods);
        return Application.router;
    }

    public static Router add(String path, Class<?> controller, Object callback, String[] allowedMethods) {
        String controllerName = controller.getSimpleName();
        Application.router.add(path, controllerName + "@" + callback, allowedMethods);

        return Application.router;
    }

    public static Router add(String path, BiConsumer<HTTPRequest, HTTPResponse> callback, String[] allowedMethods) {
        Application.router.add(path, callback, allowedMethods);
        return Application.router;
    }
}
