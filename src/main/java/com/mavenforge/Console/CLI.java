package com.mavenforge.Console;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mavenforge.Server.HTTPServer;
import com.mavenforge.Engines.Template.TemplateEngine;
import com.mavenforge.Engines.Template.TemplateLoader;

public class CLI {
    public static void main(String[] args) {
        int port = getPort(args);

        System.out.println("-------------------------------------------");
        System.out.println("Welcome to MavenForge Java MVC Framework");
        System.out.println("-------------------------------------------");

        System.out.println("Starting Development server...");

        HTTPServer server = new HTTPServer(port);

        Map<String, Object> data = new HashMap<>();
        data.put("title", "Shopping List");
        data.put("greeting", "Welcome to our store!");
        data.put("showItems", true);
        List<String> itemsList = Arrays.asList("Apples", "Bananas", "Oranges");
        data.put("items", itemsList);

        TemplateLoader templateLoader = new TemplateLoader();
        TemplateEngine engine = new TemplateEngine(templateLoader);

        String renderedContent = engine.renderFromFile(
                "/media/fullyworldwebtutorials/Projects/mavenforge/src/main/java/com/mavenforge/index.vyuha", data);
        System.out.println(renderedContent);

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
