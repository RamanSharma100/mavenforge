package com.mavenforge.Console.Commands;

import java.util.HashMap;
import java.util.Map;

import com.mavenforge.Console.Command;

public class Serve extends Command {

    public String command = "serve";
    public String description = "Start the development server";
    public Map<String, String> options = new HashMap<String, String>();
    public Map<String, String> flags = new HashMap<String, String>();

    public void execute(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }

}