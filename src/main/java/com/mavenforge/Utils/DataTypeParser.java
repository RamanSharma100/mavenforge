package com.mavenforge.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTypeParser {

    public static Object parseValue(String value) {

        if (value.equalsIgnoreCase("null")) {
            return null;
        }

        if (isJSONArray(value)) {
            return parseJSONArray(value);
        }

        if (isBoolean(value)) {
            return Boolean.parseBoolean(value);
        }

        if (isJSONObject(value)) {
            return parseJSONObject(value);
        }

        if (isInteger(value)) {
            return Integer.parseInt(value);
        }

        if (isLong(value)) {
            return Long.parseLong(value);
        }

        if (isFloat(value)) {
            return Float.parseFloat(value);
        }

        if (isDouble(value)) {
            return Double.parseDouble(value);
        }

        if (isBigDecimal(value)) {
            return new java.math.BigDecimal(value);
        }

        if (isShort(value)) {
            return Short.parseShort(value);
        }

        if (isByte(value)) {
            return Byte.parseByte(value);
        }

        if (isCharacter(value)) {
            return value.charAt(1);
        }

        if (value.contains(",")) {
            String[] values = value.split(",");
            List<Object> list = new ArrayList<>();
            for (String val : values) {
                list.add(parseValue(val.trim()));
            }
            return list;
        }

        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }

    public static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public static boolean isString(String value) {
        return value.startsWith("\"") && value.endsWith("\"");
    }

    public static boolean isNull(String value) {
        return value.equalsIgnoreCase("null");
    }

    public static boolean isDecimal(String value) {
        return isFloat(value) || isDouble(value);
    }

    public static boolean isBigDecimal(String value) {
        try {
            new java.math.BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isShort(String value) {
        try {
            Short.parseShort(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isByte(String value) {
        try {
            Byte.parseByte(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isCharacter(String value) {
        return value.startsWith("'") && value.endsWith("'");
    }

    public static boolean isPrimitive(String value) {
        return isBoolean(value) || isString(value) || isNull(value) || isNumber(value) || isCharacter(value);
    }

    public static boolean isNumber(String value) {
        return isInteger(value) || isFloat(value) || isDouble(value) || isLong(value) || isShort(value)
                || isByte(value);
    }

    public static boolean isJSONObject(String value) {
        return value.startsWith("{") && value.endsWith("}");
    }

    public static boolean isJSONArray(String value) {
        return value.startsWith("[") && value.endsWith("]");
    }

    public static Map<String, Object> parseJSONObject(String value) {
        Map<String, Object> map = new HashMap<>();
        String jsonContent = value.substring(1, value.length() - 1);

        String[] keyValuePairs = jsonContent.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : keyValuePairs) {
            String[] pairs = pair.split(":", 2);
            String key = pairs[0].trim().replaceAll("^\"|\"$", "");
            String val = pairs[1].trim().replaceAll("^\"|\"$", "");
            map.put(key, parseValue(val));
        }

        return map;

    }

    public static List<Object> parseJSONArray(String value) {
        List<Object> map = new ArrayList<>();
        String jsonContent = value.substring(1, value.length() - 1);

        String[] values = jsonContent.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String val : values) {
            map.add(parseValue(val.trim().replaceAll("^\"|\"$", "")));
        }

        return map;
    }
}
