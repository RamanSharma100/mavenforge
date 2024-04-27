package com.mavenforge.Http;

import java.util.Map;
import java.util.HashMap;

public class Router {

    private Map<String, Map<String, Object>> routes = new HashMap<String, Map<String, Object>>();
    public HTTPRequest request;
    public HTTPResponse response;

    public Router(HTTPRequest request, HTTPResponse response) {
        this.request = request;
        this.response = response;
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

    public void resolve() {
        String path = this.request.getPath();

        if (!path.equals("/") && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }

        String method = this.request.getMethod().toLowerCase();
        Object callback = this.routes.get(method).get(path);

        if (callback == null) {
            this.response.status(404).response("This " + path + " route does not exist").send();
            return;
        }

        this.response.status(200).response(callback.toString()).send();
    }

    public Map<String, Map<String, Object>> getRoutes() {
        return this.routes;
    }

}
