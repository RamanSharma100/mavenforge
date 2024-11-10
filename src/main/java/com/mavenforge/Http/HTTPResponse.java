package com.mavenforge.Http;

import java.util.Map;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

import com.mavenforge.Renderers.View;
import com.mavenforge.Utils.Constants;

public class HTTPResponse {

    private Socket socket;
    private PrintWriter writer;
    private HTTPRequest request;
    private int responseByte, statusCode;
    private Boolean statusCodeWrote = false;
    private String VIEWS_DIR = "views";
    private long startTime, endTime, responseTime;
    public transient static String rootClassPackage = Constants.rootClassPackage;

    public HTTPResponse(Socket socket, HTTPRequest request) throws IOException {
        this.socket = socket;
        this.request = request;
        this.startTime = request.getStartTime();
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        if (Constants.env != null) {
            String viewsDir = Constants.env.get("VIEWS_DIR");
            if (viewsDir != null) {
                this.VIEWS_DIR = viewsDir;
            }
        }
    }

    public HTTPResponse send(String body) {
        this.endTime = System.nanoTime();
        this.responseTime = (this.endTime - this.startTime) / 1000000;
        if (!statusCodeWrote) {
            this.statusCode = 200;
            this.status(200);
        }
        this.writer.println("Content-Type: text/html");
        this.writer.println("Content-Length: " + body.length());
        this.writer.println();
        this.writer.println(body);
        this.responseByte = body.length();

        this.writer.flush();
        System.out.println(request.getMethod() + " " + request.getPath() + "\t" + this.responseTime
                + "ms\t" + this.responseByte + " bytes\t" + this.statusCode + " - "
                + this.getStatusText(this.statusCode));

        return this;
    }

    public HTTPResponse status(int code) {

        if (this.statusCodeWrote) {
            return this;
        }

        this.statusCode = code;

        this.statusCodeWrote = true;
        this.writer.println("HTTP/1.1 " + code + " " + this.getStatusText(code));

        return this;
    }

    public void close() throws IOException {
        try {
            this.writer.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HTTPResponse render(String viewName) {
        String view = View.render(viewName, this.VIEWS_DIR);
        return this.send(view);
    }

    public HTTPResponse render(String viewName, Map<String, Object> data) {
        String view = View.render(viewName, data, this.VIEWS_DIR);
        return this.send(view);
    }

    public HTTPResponse redirect(String path) {
        this.status(302);
        this.writer.println("Location: " + path);
        return this;
    }

    public HTTPResponse json(Map<String, Object> data) {
        this.writer.println("Content-Type: application/json");
        this.writer.println("Content-Length: " + data.toString().length());
        this.writer.println();
        this.writer.println(data.toString());
        this.responseByte = data.toString().length();
        return this;
    }

    public HTTPResponse json(String data) {
        this.writer.println("Content-Type: application/json");
        this.writer.println("Content-Length: " + data.length());
        this.writer.println();
        this.writer.println(data);
        this.responseByte = data.length();
        return this;
    }

    public HTTPResponse json(Object data) {
        this.writer.println("Content-Type: application/json");
        this.writer.println("Content-Length: " + data.toString().length());
        this.writer.println();
        this.writer.println(data.toString());
        this.responseByte = data.toString().length();
        return this;
    }

    public HTTPResponse json() {
        this.writer.println("Content-Type: application/json");
        this.writer.println("Content-Length: 0");
        this.writer.println();
        this.responseByte = 0;
        return this;
    }

    private String getStatusText(int code) {
        return HTTPStatusCode.getStatusCode(code);
    }

}
