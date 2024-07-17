package com.mavenforge.Console;

import java.util.Map;

public class Executer {
    protected Map<String, Command> defaultCommands = Map.of(
            "serve", new com.mavenforge.Console.Commands.Serve(),
            "database", new com.mavenforge.Console.Commands.Database.Database());

    protected boolean commandExists(String command) {
        return defaultCommands.containsKey(command);
    }
}
