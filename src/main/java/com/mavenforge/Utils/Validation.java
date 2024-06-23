package com.mavenforge.Utils;

import com.mavenforge.Application;

import java.lang.ClassNotFoundException;

public class Validation {
    public static boolean isRoutesFilePresent(String packageName) {
        try {
            String routesPackage = Application.rootClassPackage + ".routes.Web";
            Class<?> routesClass = ImportClass.fromPackage(routesPackage);

            if (routesClass != null) {
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
