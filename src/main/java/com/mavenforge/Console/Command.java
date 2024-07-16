package com.mavenforge.Console;

import java.util.HashMap;
import java.util.Map;

import com.mavenforge.Application;

public abstract class Command {

    public String command = "";
    public String description = "";
    public Map<String, String> options = new HashMap<String, String>();
    public Map<String, String> flags = new HashMap<String, String>();

    public void execute(String[] args, String packageName) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(packageName);

            Application.run(clazz, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    };
}
