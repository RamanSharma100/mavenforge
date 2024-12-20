package com.mavenforge.Http;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.HashMap;
import java.lang.reflect.Method;

import java.lang.reflect.InvocationTargetException;

import com.mavenforge.Application;
import com.mavenforge.Services.Cookie;

public class Router {

    private Map<String, Map<String, Object>> routes = new HashMap<String, Map<String, Object>>();
    public HTTPRequest request;
    public HTTPResponse response;
    public HTTPContext context;

    public Router(HTTPRequest request, HTTPResponse response, HTTPContext context) {
        this.request = request;
        this.response = response;
        this.context = context;
    }

    public Router get(String path, Object callback) {
        if (!this.routes.containsKey("get")) {
            this.routes.put("get", new HashMap<String, Object>());
            this.routes.get("get").put(path, callback);

        } else {
            this.routes.get("get").put(path, callback);
        }

        return this;
    }

    public Router post(String path, Object callback) {
        if (!this.routes.containsKey("post")) {
            this.routes.put("post", new HashMap<String, Object>());
            this.routes.get("post").put(path, callback);

        } else {
            this.routes.get("post").put(path, callback);
        }

        return this;
    }

    public Router add(String path, Object callback) {
        String[] methods = { "get", "post", "patch", "put", "options", "delete" };

        for (String method : methods) {
            if (!this.routes.containsKey(method)) {
                this.routes.put(method, new HashMap<String, Object>());
                this.routes.get(method).put(path, callback);
            } else {
                this.routes.get(method).put(path, callback);
            }
        }

        return this;
    }

    public Router add(String path, Object callback, String[] allowedMethods) {
        String[] methods = allowedMethods;

        for (String method : methods) {
            method = method.toLowerCase();
            if (!this.routes.containsKey(method)) {
                this.routes.put(method, new HashMap<String, Object>());
                this.routes.get(method).put(path, callback);
            } else {
                this.routes.get(method).put(path, callback);
            }
        }

        return this;
    }

    public void resolve() {
        String path = this.request.getPath();
        Cookie sessionCookie = this.request.getCookie("SESSION_ID");
        System.out.println("Session ID: " + sessionCookie.getValue());

        if (!path.equals("/") && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }

        String method = this.request.getMethod().toLowerCase();
        Object callback = this.routes.get(method).get(path);

        if (callback == null) {
            this.response.status(404).send("This " + path + " route does not exist");
            return;
        }

        if (callback instanceof String) {
            if (callback.toString().contains("@")) {
                String controllerName = callback.toString().split("@")[0];
                String methodName = callback.toString().split("@")[1];

                try {
                    String controllerPackage = Application.rootClassPackage
                            + ".controllers." + controllerName;
                    Class<?> controller = Class.forName(controllerPackage);

                    this.invokeControllerMethod(controller, methodName, controllerName, controllerPackage);
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                    this.response.status(500).send("Controller '" + controllerName + "' not found, Create " +
                            controllerName + ".java in " + Application.rootClassPackage + ".controllers package");
                    return;
                }

            } else {
                this.response.status(200).send("Render view: " + callback.toString() + ".html");
                return;
            }
        }

        @SuppressWarnings("unchecked")
        BiConsumer<HTTPRequest, HTTPResponse> func = (BiConsumer<HTTPRequest, HTTPResponse>) (BiConsumer<?, ?>) callback;
        func.accept(this.request, this.response);
        return;
    }

    public Map<String, Map<String, Object>> getRoutes() {
        return this.routes;
    }

    private void invokeControllerMethod(Class<?> controller, String methodName, String controllerName,
            String controllerPackage) {
        try {
            Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            Method[] methods = controller.getDeclaredMethods();
            Method methodToInvoke = null;

            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    if (method.getParameterCount() == 0) {
                        methodToInvoke = method;
                        break;
                    } else if (method.getParameterCount() == 1) {
                        if (method.getParameterTypes()[0] == HTTPRequest.class) {
                            methodToInvoke = method;
                            break;
                        } else if (method.getParameterTypes()[0] == HTTPResponse.class) {
                            methodToInvoke = method;
                            break;
                        }
                    }

                    else if (method.getParameterCount() == 2
                            && method.getParameterTypes()[0] == HTTPRequest.class
                            && method.getParameterTypes()[1] == HTTPResponse.class) {
                        methodToInvoke = method;
                        break;
                    }
                }
            }

            if (methodToInvoke != null) {
                if (methodToInvoke.getParameterCount() == 0) {
                    methodToInvoke.invoke(controllerInstance);
                } else if (methodToInvoke.getParameterCount() == 1) {
                    if (methodToInvoke.getParameterTypes()[0] == HTTPRequest.class) {
                        methodToInvoke.invoke(controllerInstance, request);
                    } else if (methodToInvoke.getParameterTypes()[0] == HTTPResponse.class) {
                        methodToInvoke.invoke(controllerInstance, response);
                    }
                } else if (methodToInvoke.getParameterCount() == 2) {
                    methodToInvoke.invoke(controllerInstance, request, response);
                }
            } else {
                response.status(500)
                        .send("Method '" + methodName + "' with expected signatures not found in controller '"
                                + controllerPackage + "'");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            System.out.println(e);
            response.status(500)
                    .send("Error invoking method '" + methodName + "' in controller '" + controllerPackage + "'");
        }
    }

}
