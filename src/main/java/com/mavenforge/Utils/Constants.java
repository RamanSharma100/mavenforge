package com.mavenforge.Utils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.cdimascio.dotenv.Dotenv;

public class Constants {
    public static Dotenv env = null;
    public static String rootClassPackage = null;
    public static final String TEMPLATE_EXTENSION = ".vyuha";

    public static enum DatabaseType {
        mysql, postgresql, sqlite, mongodb
    }

    public static void setRootClassPackage(String rootClassPackage) {
        Constants.rootClassPackage = rootClassPackage;
    }

    public static String getRootClassPackage() {
        return Constants.rootClassPackage;
    }

    public static String getTEMPLATE_EXTENSION() {
        return Constants.TEMPLATE_EXTENSION;
    }

    public static String getPackagePath() {
        String packageName = Constants.rootClassPackage;
        String resourcePath = packageName.replace(".", "/");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }

        Path path = Paths.get(resource.getPath()).toAbsolutePath();

        return path.toString();
    }

    public static String getPath(String VIEWS_DIR) {
        String pakageName = Constants.rootClassPackage;
        String resourcePath = pakageName.replace(".", "/") + "/" + VIEWS_DIR;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }

        Path path = Paths.get(resource.getPath()).toAbsolutePath();

        return path.toString();
    }

    public static String getResourcePath(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }

        Path path = Paths.get(resource.getPath()).toAbsolutePath();

        return path.toString();
    }

}
