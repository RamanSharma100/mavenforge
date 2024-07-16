package com.mavenforge.Console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Forge {

    private static void help() {
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
    }

    public static void main(String[] args) {

        String command = args.length > 0 ? args[0] : "";

        if (command.isEmpty() || command == null) {
            System.out.println("---------- Forge CLI -----------");
            Forge.help();
        } else {
            String[] commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

            String packageName = getPackageName();

            if (packageName.isEmpty()) {
                System.out.println("Could not find entry point for the application");
                System.out.println("Please make sure you have a Main.java file in the src/main/java directory");
                System.exit(0);
                return;
            }

            switch (command) {
                case "serve":
                    System.out.println(getPackageName());
                    Executer executer = new Executer();
                    executer.defaultCommands.get("serve").execute(commandArgs, packageName);
                    break;
                default:
                    System.out.println("Command not found");
                    Forge.help();
                    break;
            }
        }
    }

    private static String getPackageName() {
        String currentDir = System.getProperty("user.dir");
        File srcDir = new File(currentDir, "src/main/java");

        List<File> javaFiles = null;
        try {
            javaFiles = Files.walk(Paths.get(srcDir.getPath()))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(path -> path.toFile())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (javaFiles != null && !javaFiles.isEmpty()) {
            for (File javaFile : javaFiles) {
                if (javaFile.getName().equals("Main.java")) {
                    return extractPackageName(javaFile) + "." + javaFile.getName().replace(".java", "");
                }
                try {
                    List<String> lines = Files.readAllLines(javaFile.toPath());
                    for (String line : lines) {
                        if (line.contains("import com.mavenforge.Application")
                                || line.contains("import com.mavenforge.HTTPServer")) {
                            return extractPackageName(javaFile) + "." + javaFile.getName().replace(".java", "");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    private static String extractPackageName(File javaFile) {
        try {
            List<String> lines = Files.readAllLines(javaFile.toPath());
            for (String line : lines) {
                if (line.startsWith("package ")) {
                    return line.replace("package ", "").replace(";", "").trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
