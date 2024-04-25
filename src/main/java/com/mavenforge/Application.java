package com.mavenforge;

import java.net.ServerSocket;

import com.mavenforge.Http.Router;
import com.mavenforge.Http.HTTPRequest;
import com.mavenforge.Http.HTTPResponse;
import com.mavenforge.Server.HTTPServer;

public class Application {
    public static Router router;
    public static ServerSocket server;
    public static HTTPRequest request;
    public static HTTPResponse response;

    public static void run(String[] args) {

        int port = getPort(args);

        System.out.println("Package Name Where this function is called: " + Application.class.getPackageName());

        HTTPServer server = new HTTPServer(port);

        System.out.println("-------------------------------------------");
        System.out.println("Welcome to MavenForge Java MVC Framework");
        System.out.println("-------------------------------------------");

        System.out.println("Starting Development server...");

        server.start();

        Application.router = server.getRouter();
        Application.server = server.getServer();
        Application.request = server.getRequest();

    }

    private static int getPort(String[] args) {

        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port 8080.");
            }
        }

        String portStr = System.getenv("PORT");
        if (portStr != null && !portStr.isEmpty()) {
            try {
                return Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number in environment variable PORT. Using default port 8080.");
            }
        }

        return 8080;
    }

}
