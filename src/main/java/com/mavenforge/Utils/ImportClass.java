package com.mavenforge.Utils;

public class ImportClass {
    public static Class<?> fromPackage(String packageName) throws ClassNotFoundException {
        try {
            return Class.forName(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object fromClass(Class<?> clazz) throws ClassNotFoundException {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
