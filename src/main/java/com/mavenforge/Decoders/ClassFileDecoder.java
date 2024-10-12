package com.mavenforge.Decoders;

import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.benf.cfr.reader.Main;

public class ClassFileDecoder {
    public static String decompileFile(String classFilePath) {
        String className = classFilePath.substring(classFilePath.lastIndexOf("/") + 1);

        String outputPath = classFilePath.replace(classFilePath.substring(classFilePath.lastIndexOf("/") + 1),
                "decompiled");
        String[] args = { classFilePath, "--outputdir", outputPath };
        Main.main(args);
        String filePath = classFilePath.replace(classFilePath.substring(classFilePath.lastIndexOf("/") + 1),
                "decompiled") + "/"
                + classFilePath.substring(
                        classFilePath.indexOf("/target/classes/") + "/target/classes".length(),
                        classFilePath.indexOf(className) - 1)
                + "/" + className.replace(".class", ".java");

        try {
            Path path = Paths.get(filePath);

            String classNameWithPath = readDecompiledFile(path);
            return classNameWithPath;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Class<?> decompileClass(String classFilePath) {
        String className = classFilePath.substring(classFilePath.lastIndexOf("/") + 1);

        String outputPath = classFilePath.replace(classFilePath.substring(classFilePath.lastIndexOf("/") + 1),
                "decompiled");
        String[] args = { classFilePath, "--outputdir", outputPath };
        Main.main(args);
        String filePath = classFilePath.replace(classFilePath.substring(classFilePath.lastIndexOf("/") + 1),
                "decompiled")
                + classFilePath.substring(
                        classFilePath.indexOf("/target/classes/") + "/target/classes".length(),
                        classFilePath.indexOf(className) - 1)
                + "/" + className.replace(".class", ".java");

        try {
            ClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File(filePath).toURI().toURL() },
                    ClassFileDecoder.class.getClassLoader());

            Class<?> clazz = Class.forName(filePath, true, classLoader);

            return clazz;

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String readDecompiledFile(Path javaFilePath) throws IOException {
        return new String(Files.readAllBytes(javaFilePath));
    }
}
