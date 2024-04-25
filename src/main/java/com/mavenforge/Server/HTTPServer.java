package com.mavenforge.Server;

import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

import com.mavenforge.Http.Router;
import com.mavenforge.Http.HTTPRequest;
import com.mavenforge.Http.HTTPResponse;

public class HTTPServer {
    private int port;
    private int serverTry = 0;
    private ServerSocket serverSocket;
    private HTTPRequest request;
    private HTTPResponse response;
    private Router router;

    public HTTPServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(port);
            this.serverTry++;
            System.out.println("Server started on http://localhost:" + this.port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (clientSocket != null) {
                    handleRequest(clientSocket);
                }
            }
        } catch (IOException e) {
            System.out.println(
                    this.port + " is already in use. Trying " + (this.port + 1) + "... " + (this.serverTry + 1)
                            + " / 5");
            this.port++;
            if (this.serverTry < 5) {
                start();
            } else {
                System.out.println("Server could not start. Exiting...");
                System.exit(1);
            }

        }
    }

    private void handleRequest(Socket clientSocket) {
        try {

            this.request = new HTTPRequest(clientSocket);
            this.response = new HTTPResponse(clientSocket, request);

            this.router = new Router(request, response);

            if (this.request.getPath() == null) {
                clientSocket.close();
                return;
            }

            this.router.get("/", "Hello from MavenForge Java MVC Framework");

            this.router.resolve();

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Router getRouter() {
        return this.router;
    }

    public ServerSocket getServer() {
        return this.serverSocket;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }

}
