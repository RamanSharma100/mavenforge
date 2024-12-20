package com.mavenforge.Http;

import java.util.Map;

import com.mavenforge.Services.Cookie;
import com.mavenforge.Services.HttpSession;
import com.mavenforge.Utils.DataTypeParser;

import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HTTPRequest {

    private String path;
    private String method;
    private long startTime;
    private Map<String, Object> body;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Map<String, String> searchParams;
    private String sessionId;

    public HTTPRequest(Socket socket) throws IOException {
        this.body = new HashMap<>();
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
        this.searchParams = new HashMap<>();

        this.startTime = System.nanoTime();
        parseRequest(socket);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Cookie getCookie(String key) {
        return Cookie.get(key) != null ? Cookie.get(key) : null;
    }

    private void parseRequest(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String requestLine = reader.readLine();

        if (requestLine != null) {
            String[] requestLineParts = requestLine.split("\\s+");
            this.method = requestLineParts[0];
            this.path = requestLineParts[1];

            String header;
            while ((header = reader.readLine()) != null && !header.isEmpty()) {
                String[] headerParts = header.split(":");
                this.headers.put(headerParts[0].trim(), headerParts[1].trim());
            }

            StringBuilder bodyBuilder = new StringBuilder();
            while (reader.ready()) {
                bodyBuilder.append((char) reader.read());
            }

            String bodyString = bodyBuilder.toString();
            if (!bodyString.isEmpty()) {
                String[] bodyParts = bodyString.split("&");
                for (String part : bodyParts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        Object value = DataTypeParser.parseValue(keyValue[1]);
                        this.body.put(key, value);
                    }
                }
            }

            // check if there are any query parameters
            if (this.path.contains("?")) {
                String[] pathParts = this.path.split("\\?");
                this.path = pathParts[0];
                String[] queryParams = pathParts[1].split("&");
                for (String queryParam : queryParams) {
                    String[] queryParamParts = queryParam.split("=");
                    this.searchParams.put(queryParamParts[0], queryParamParts[1]);
                }
            }

            // check if there are any path parameters
            if (this.path.contains("/")) {
                String[] pathParts = this.path.split("/");
                for (int i = 0; i < pathParts.length; i++) {
                    if (i % 2 != 0) {
                        this.params.put(pathParts[i - 1], pathParts[i]);
                    }
                }
            }

        }

        String sessionId = Cookie.get("SESSION_ID") != null ? Cookie.get("SESSION_ID").getValue() : null;

        if (sessionId == null || !HttpSession.isActive(sessionId)) {
            sessionId = HttpSession.start();
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public Map<String, String> getSearchParams() {
        return searchParams;
    }

    public String getSearchParam(String key) {
        return searchParams.get(key);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String toString() {
        return "Method: " + method + "\nPath: " + path + "\nHeaders: " + headers + "\nBody: " + body;
    }

    public long getStartTime() {
        return startTime;
    }

}
