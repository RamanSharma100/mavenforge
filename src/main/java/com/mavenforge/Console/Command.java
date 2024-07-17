package com.mavenforge.Console;

import java.util.HashMap;
import java.util.Map;

public abstract class Command {

    public String command = "";
    public String description = "";
    public Map<String, String> options = new HashMap<String, String>();
    public Map<String, String> flags = new HashMap<String, String>();

    public void execute(String[] args, String packageName) {
    };
}
