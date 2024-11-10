package com.mavenforge.Http;

public class HTTPContext {
    public HTTPRequest request;
    public HTTPResponse response;

    public HTTPContext(HTTPRequest request, HTTPResponse response) {
        this.request = request;
        this.response = response;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }

    public HTTPResponse getResponse() {
        return this.response;
    }

    public String getMethod() {
        return this.request.getMethod();
    }

    public String getPath() {
        return this.request.getPath();
    }

    public Object getBody() {
        return this.request.getBody();
    }

    public void response(String body) {
        this.response.send(body);
    }

}
