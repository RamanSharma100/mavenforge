package com.mavenforge.Console;

public class Forge {
    public static void main(String[] args) {
        String command = args.length > 1 ? args[1] : "";

        if (command.isEmpty() || command == null) {
            System.out.println("---------- Forge CLI -----------");
            System.out.println();
            System.out.println("Usage: forge <command> [options]");
            System.out.println();
            System.out.println("Commands:");
            System.out.println();
            System.out.println("help                                Display help");
            System.out.println("serve                               Start the server");
            System.out.println("migrate [options]                   Run the migrations");
            System.out.println("make <scaffold> <name> [options]    Generate a scaffold");
            System.out.println();
            System.out.println("Scaffold:");
            System.out.println();
            System.out.println("view <name> [options]           Generate a view");
            System.out.println("seeder <name> [options]         Generate a seed");
            System.out.println("model <name> [options]          Generate a model");
            System.out.println("migration <name> [options]      Generate a migration");
            System.out.println("controller <name> [options]     Generate a controller");
        } else {
            System.out.println("Command: " + command);
        }
    }
}
