package com.mavenforge.Formatter;

public class Path {
    public static String getPath(String dottedPath) {
        return dottedPath.replace(".", "/");
    }

    public static String getFileName(String path) {
        String[] pathParts = path.split("/");
        return pathParts[pathParts.length - 1];
    }

    public static String getFileNameWithoutExtension(String path) {
        String fileName = getFileName(path);
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[0];
    }

    public static String getExtension(String path) {
        String fileName = getFileName(path);
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[fileNameParts.length - 1];
    }

    public static String getDirectory(String path) {
        String[] pathParts = path.split("/");
        String directory = "";
        for (int i = 0; i < pathParts.length - 1; i++) {
            directory += pathParts[i] + "/";
        }
        return directory;
    }
}