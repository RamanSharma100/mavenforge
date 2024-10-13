package com.mavenforge.Database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mavenforge.Application;
import com.mavenforge.Decoders.ClassFileDecoder;

public abstract class Model {
    protected static String table;
    protected static Class<?> modelName;
    protected static String migration;
    protected static List<String> attributes;
    protected static Object db = Application.database.db;

    static class ModelFields {
        private String table;
        private String[] fillables;
        private String[] guarded;

        public ModelFields() {
        }

        public ModelFields(String[] fillables, String[] guarded) {
            this.fillables = fillables;
            this.guarded = guarded;
        }

        public ModelFields(String table, String[] fillables, String[] guarded) {
            this.table = table;
            this.fillables = fillables;
            this.guarded = guarded;
        }

        public String getTable() {
            return table;
        }

        public String[] getFillables() {
            return fillables;
        }

        public String[] getGuarded() {
            return guarded;
        }

        public Map<String, Object> getAttributes() {
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("table", table);
            attributes.put("fillables", fillables);
            attributes.put("guarded", guarded);
            return attributes;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public void setFillables(String[] fillables) {
            this.fillables = fillables;
        }

        public void setGuarded(String[] guarded) {
            this.guarded = guarded;
        }

        public void addFillable(String fillable) {
            String[] newFillables = new String[fillables.length + 1];
            for (int i = 0; i < fillables.length; i++) {
                newFillables[i] = fillables[i];
            }
            newFillables[fillables.length] = fillable;
            fillables = newFillables;
        }

        public void addGuarded(String guardedField) {
            String[] newGuardedArray = new String[this.guarded.length + 1];
            for (int i = 0; i < this.guarded.length; i++) {
                newGuardedArray[i] = this.guarded[i];
            }
            newGuardedArray[guarded.length] = guardedField;
            guarded = newGuardedArray;
        }

        public void removeFillable(String fillable) {
            String[] newFillables = new String[fillables.length - 1];
            int j = 0;
            for (int i = 0; i < fillables.length; i++) {
                if (fillables[i].equals(fillable)) {
                    continue;
                }
                newFillables[j] = fillables[i];
                j++;
            }
            fillables = newFillables;
        }

        public void removeGuarded(String guardedField) {
            String[] newGuardedArray = new String[this.guarded.length - 1];
            int j = 0;
            for (int i = 0; i < this.guarded.length; i++) {
                if (this.guarded[i].equals(guardedField)) {
                    continue;
                }
                newGuardedArray[j] = this.guarded[i];
                j++;
            }
            guarded = newGuardedArray;
        }

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
        ModelFields modelProtectedFields = getProtectedFields(modelName);

        String table = modelProtectedFields.getTable();
        String[] guarded = modelProtectedFields.getGuarded();
        String[] fillables = modelProtectedFields.getFillables();


        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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

    private static ModelFields getProtectedFields(String modelName) {
        String modelsFolder = Application.rootClassPackage + ".models";
        String modelFilePath = modelsFolder + "." + modelName;
        System.out.println("Models Folder: " + modelsFolder);
        System.out.println("Model File Path: " + modelFilePath);

        String modifiedTableName = modelName.toLowerCase() + "s";
        ModelFields modelFields = new Model.ModelFields();

        try {
            Class<?> modelClass = Class.forName(modelFilePath);
            Object modelInstance = modelClass.getDeclaredConstructor().newInstance();
            // Get the protected class variables
            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (Modifier.isProtected(field.getModifiers())) {
                    if (field.getName().equals("table")) {
                        String table = (String) field.get(modelInstance);
                        if (table == null || table.isEmpty()) {
                            modelFields.setTable(modifiedTableName);
                        } else {
                            modelFields.setTable(table);
                        }
                    } else if (field.getName().equals("fillable")) {
                        String[] fillables = (String[]) field.get(modelInstance);
                        modelFields.setFillables(fillables);
                    } else if (field.getName().equals("guarded")) {
                        String[] guarded = (String[]) field.get(modelInstance);
                        modelFields.setGuarded(guarded);
                    }
                }
            }

        } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return modelFields;
    }
}