package com.mavenforge.Http;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class HTTPResponse {

    private Socket socket;
    private PrintWriter writer;
    private HTTPRequest request;
    private int responseByte, statusCode;
    private Boolean statusCodeWrote = false;
    private long startTime, endTime, responseTime;

    public HTTPResponse(Socket socket, HTTPRequest request) throws IOException {
        this.socket = socket;
        this.request = request;
        this.startTime = request.getStartTime();
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public HTTPResponse response(String body) {
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

    public HTTPResponse send() {
        this.writer.flush();
        System.out.println(request.getMethod() + " " + request.getPath() + "\t" + this.responseTime
                + "ms\t" + this.responseByte + " bytes\t" + this.getStatusText(this.statusCode));

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

    private String getStatusText(int code) {
        return HTTPStatusCode.getStatusCode(code);
    }

}