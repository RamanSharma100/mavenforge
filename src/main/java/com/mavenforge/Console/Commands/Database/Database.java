package com.mavenforge.Console.Commands.Database;

import java.util.HashMap;
import java.util.Map;

import com.mavenforge.Console.Command;

public class Database extends Command {
    public String command = "database";
    public String description = "Work with the database";
    public Map<String, String> options = new HashMap<String, String>();
    public Map<String, String> flags = new HashMap<String, String>();

    protected Map<String, Command> defaultCommands = Map.of(
            "migrate", new com.mavenforge.Console.Commands.Database.Migrate());

    private void help() {
        System.out.println();
        System.out.println("Usage: forge database <DBScaffold> [options]");
        System.out.println();
        System.out.println("DBScaffold:");
        System.out.println();
        System.out.println("drop <name> [options]           Drop a database");
        System.out.println("create <name> [options]         Create a database");
        System.out.println("dump <name> [options]           Dump the database");
        System.out.println("migrate <name> [options]        Run the migrations");
        System.out.println("rollback <name> [options]       Rollback the migrations");
    }

    public void execute(String[] args, String packageName) {
        String command = args.length > 0 ? args[0] : "";

        if (command.isEmpty() || command == null) {
            System.out.println("---------- Forge CLI 'database' Command -----------");
            this.help();
        } else {
            String[] commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

            switch (command) {
                case "migrate":
                    this.defaultCommands.get("migrate").execute(commandArgs, packageName);
                    break;
                default:
                    System.out.println("Invalid command");
                    this.help();
                    break;
            }
        }
    }
}
