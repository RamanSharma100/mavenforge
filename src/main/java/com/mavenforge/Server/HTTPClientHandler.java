package com.mavenforge.Server;

import java.net.Socket;
import com.mavenforge.Http.Router;
import com.mavenforge.Application;
import com.mavenforge.Http.HTTPRequest;
import com.mavenforge.Http.HTTPResponse;

public class HTTPClientHandler implements Runnable {
    private Socket clientSocket;
    private HTTPResponse response;
    private HTTPRequest request;

    public HTTPClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    private void handleRequest() {
        try {

            this.request = new HTTPRequest(this.clientSocket);
            this.response = new HTTPResponse(this.clientSocket, this.request);

            Application.router = new Router(this.request, this.response);

            if (this.request.getPath() == null) {
                clientSocket.close();
                return;
            }

            Application.router.get("/", "Hello from MavenForge Java MVC Framework");

            Application.router.resolve();

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleRequest();
    }

}
