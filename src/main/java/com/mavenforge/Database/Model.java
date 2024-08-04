package com.mavenforge.Database;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import com.mavenforge.Decoders.ClassfileDecoder;

public abstract class Model {
    protected static String table;
    protected static Class<?> modelName;
    protected static String migration;
    protected static List<String> attributes;

    private static void getClassAttributes(Class<?> className) {
        System.out.println("Class Name: " + className.getName());
    }

    public static <T> void find(T $id) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        Integer lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

        Class<?> invokingClass = null;

        try {
            invokingClass = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }

        System.out.println("Method Name: " + methodName);

        Method method = null;

        for (Method m : invokingClass.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }

        String classFilePath = invokingClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        classFilePath += "/" + className.replace(".", "/") + ".class";
        File file = new File(classFilePath);

        // System.out.println("Class File Path: " + classFilePath);
        // System.out.println("Class Name: " + className);

        String content = "";

        try {
            content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

        ClassfileDecoder decoder = new ClassfileDecoder(classFilePath);

        System.out.println(decoder.decode());

    }

}
