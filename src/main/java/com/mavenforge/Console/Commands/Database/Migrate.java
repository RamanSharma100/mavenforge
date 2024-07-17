package com.mavenforge.Console.Commands.Database;

import java.io.File;
import java.util.List;

import javax.naming.spi.DirectoryManager;

import com.mavenforge.Console.Command;

import kotlin.collections.builders.ListBuilder;

public class Migrate extends Command {
    public String command = "migrate";
    public String description = "Run the migrations";
    public String[] options = { "name" };
    public String[] flags = { "--force", "-f", "--help", "-h" };

    private String packageName = "";

    private List<Class<?>> getMigrationClasses() {
        String currentDir = System.getProperty("user.dir");
        File srcDir = new File(currentDir, "src/main/java/");
        String[] packageParts = this.packageName.toString().split("\\.");
        String packagePath = "";

        for (int i = 0; i < packageParts.length - 1; i++) {
            packagePath += packageParts[i] + "/";
        }

        File packageDir = new File(srcDir, packagePath + "/migrations");

        if (!packageDir.exists()) {
            System.out.println("No migrations found");
            System.exit(0);
        }

        if (!packageDir.isDirectory()) {
            System.out.println("Migrations is not a directory");
            System.exit(0);
        }

        File[] migrationFiles = packageDir.listFiles();

        if (migrationFiles.length == 0) {
            System.out.println("Migrations directory is empty");
            System.exit(0);
        }

        List<Class<?>> migrationClasses = new ListBuilder<Class<?>>();

        for (File file : migrationFiles) {
            String fileName = file.getName();
            String className = fileName.substring(0, fileName.lastIndexOf('.'));
            try {
                Class<?> migrationClass = Class.forName(packagePath.replace("/", ".") + "migrations." + className);
                migrationClasses.add(migrationClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return migrationClasses;

    }

    private void help() {
        System.out.println();
        System.out.println("Usage: forge database migrate <name> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println();
        System.out.println("-f                             Force the migration");
        System.out.println("--force                        Force the migration");
        System.out.println("--name <name>                  The name of the migration");
        System.out.println("-h                             Display info about the command");
        System.out.println("--help                         Display info about the command");
    }

    public void execute(String[] args, String packageName) {
        this.packageName = packageName;

        List<Class<?>> migrationClasses = this.getMigrationClasses();

        for (Class<?> _class : migrationClasses) {
            ClassLoader classLoader = _class.getClassLoader();
            try {
                Class<?> migrationClass = classLoader.loadClass(_class.getName());
                Object migration = migrationClass.getDeclaredConstructor().newInstance();
                migrationClass.getMethod("up").invoke(migration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // private void get

}
