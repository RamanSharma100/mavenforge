package com.mavenforge.Console;

import com.mavenforge.Server.HTTPServer;

public class CLI {
    public static void main(String[] args) {
        int port = getPort(args);

        System.out.println("-------------------------------------------");
        System.out.println("Welcome to MavenForge Java MVC Framework");
        System.out.println("-------------------------------------------");

        System.out.println("Starting Development server...");

        HTTPServer server = new HTTPServer(port);
        server.start();

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
