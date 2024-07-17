package com.mavenforge.Console.Commands;

import java.util.HashMap;
import java.util.Map;

import com.mavenforge.Application;
import com.mavenforge.Console.Command;

public class Serve extends Command {

    public String command = "serve";
    public String description = "Start the development server";
    public Map<String, String> options = new HashMap<String, String>();
    public Map<String, String> flags = new HashMap<String, String>();

    public void execute(String[] args, String packageName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(packageName);
            if (clazz == null) {
                System.out.println("Could not find entry point for the application");
                System.out.println("Please make sure you have a Main.java file in the src/main/java directory");
                System.exit(0);
                return;
            }
            Application.run(clazz, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}