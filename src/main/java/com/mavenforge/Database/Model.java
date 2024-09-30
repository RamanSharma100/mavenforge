package com.mavenforge.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import com.mavenforge.Decoders.ClassFile.ClassNode;
import com.mavenforge.Decoders.ClassFile.Decompiler;

public abstract class Model {
    protected static String table;
    protected static Class<?> modelName;
    protected static String migration;
    protected static List<String> attributes;

    private static void getClassAttributes(Class<?> className) {
        System.out.println("Class Name: " + className.getName());
    }

    public static <T> void find(T $id) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();

        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String classFilePath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath()
                + clazz.getName().replace(".", "/") + ".class";

        try {
            Decompiler decompiler = new Decompiler();
            ClassNode classNode = decompiler.decompile(classFilePath);
            getClassAttributes(clazz);
            System.out.println(decompiler.reconstructClass(classNode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}