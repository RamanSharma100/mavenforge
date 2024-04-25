package com.mavenforge.Http;

import java.util.Map;
import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HTTPRequest {

    private String path;
    private String body;
    private String method;
    private Map<String, String> headers;
    private long startTime;

    public HTTPRequest(Socket socket) throws IOException {
        this.headers = new HashMap<>();
        this.startTime = System.nanoTime();
        parseRequest(socket);
    }

    private void parseRequest(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String requestLine = reader.readLine();

        if (requestLine != null) {
            String[] requestLineParts = requestLine.split("\\s+");
            this.method = requestLineParts[0];
            this.path = requestLineParts[1];

            // System.out.println("Request: " + method + " " + path);

            String header;
            while ((header = reader.readLine()) != null && !header.isEmpty()) {
                String[] headerParts = header.split(":");
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
            StringBuilder bodyBuilder = new StringBuilder();
            while (reader.ready()) {
                bodyBuilder.append((char) reader.read());
            }

            this.body = bodyBuilder.toString();
        }

        // System.out.println("Headers: " + headers);

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
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
