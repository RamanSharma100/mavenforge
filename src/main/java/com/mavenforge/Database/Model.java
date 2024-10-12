package com.mavenforge.Database;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.mavenforge.Decoders.ClassFileDecoder;

public abstract class Model {
    protected static String table;
    protected static Class<?> modelName;
    protected static String migration;
    protected static List<String> attributes;

    @SuppressWarnings("unused")
    private static Method getMethod(Class<?> sourceClass, String methodName) {

        // Get the attributes of the class
        Method[] methods = sourceClass.getDeclaredMethods();
        Method requiredMethod = null;

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                requiredMethod = method;
                break;
            }
        }

        return requiredMethod;

    }

    public static <T> void find(T $id) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String modelName = Model.getModel(Thread.getAllStackTraces().keySet(), clazz, methodName, "find");
        System.out.println("Model Name: " + modelName);

    }

    private static String getModel(Set<Thread> trace, Class<?> clazz, String methodName, String modelMethod) {

        String classFilePath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath()
                + clazz.getName().replace(".", "/") + ".class";

        try {
            String decompiledFileContent = ClassFileDecoder.decompileFile(classFilePath);

            int skippedLines = countSkippedLines(decompiledFileContent);
            boolean isExtended = clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class);
            String line = "";
            for (Thread t : trace) {
                if (t.getStackTrace().length == 0) {
                    continue;
                }
                if (!line.equals("")) {
                    break;
                }
                for (StackTraceElement element : t.getStackTrace()) {
                    if (!line.equals("")) {
                        break;
                    }
                    if (element.getMethodName().equals(methodName)) {
                        int lineNumber = element.getLineNumber();
                        String[] lines = decompiledFileContent.split("\n");
                        int adjustedLineNumber = lineNumber + skippedLines;
                        if (isExtended) {
                            adjustedLineNumber += 1;
                        }
                        line = lines[adjustedLineNumber - 2];

                    }
                }
            }

            // split the line with method name
            String[] parts = line.split("." + modelMethod);

            return parts[0].trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int countSkippedLines(String decompiledFileContent) {
        String[] lines = decompiledFileContent.split("\n");
        int skippedLines = 0;
        boolean inCommentBlock = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("* Decompiled with CFR")) {
                inCommentBlock = true;
                skippedLines++;
            }
            if (inCommentBlock) {
                skippedLines++;

                if (line.endsWith("*/")) {
                    inCommentBlock = false;
                }
            } else if (line.startsWith("package")) {
                break;
            } else {
                continue;
            }
        }

        return skippedLines;
    }
}