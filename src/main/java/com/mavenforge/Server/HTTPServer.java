package com.mavenforge.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import com.mavenforge.Http.Router;
import com.mavenforge.Http.HTTPRequest;

public class HTTPServer {
    private int port;
    private Router router;
    private int serverTry = 0;
    private HTTPRequest request;
    private ServerSocket serverSocket;
    private static ArrayList<HTTPClientHandler> clients = new ArrayList<>();
    private static ExecutorService executorPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

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
                    HTTPClientHandler client = new HTTPClientHandler(clientSocket);
                    clients.add(client);

                    executorPool.execute(client);
                }

            }
        } catch (IOException e) {
            System.out.println(
                    this.port + " is already in use. Trying " + (this.port + 1) + "... " + (this.serverTry + 1)
                            + " / 5");
            this.port++;
            if (this.serverTry < 5 && this.port < 65535) {
                start();
            } else {
                System.out.println("Server could not start. Exiting...");
                System.exit(1);
            }

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
