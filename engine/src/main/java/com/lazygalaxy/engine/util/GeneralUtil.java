package com.lazygalaxy.engine.util;

import org.apache.commons.lang3.StringUtils;

public class GeneralUtil {
    public static String alphanumerify(String str) {
        return alphanumerify(str, "", "");
    }

    public static String alphanumerify(String str, String other, String replacement) {
        return StringUtils.stripAccents(str).toLowerCase().replaceAll("[^a-z0-9" + other + "]", replacement);
    }

    public static String numerify(String str) {
        return numerify(str, "", "");
    }

    public static String numerify(String str, String other, String replacement) {
        if (str != null) {
            return str.toLowerCase().replaceAll("[^0-9" + other + "]", replacement);
        }
        return null;
    }

    public static String alphify(String str) {
        return StringUtils.stripAccents(str).toLowerCase().replaceAll("[^a-z]", "");
    }

    public static String[] split(String str, String regex) {
        String[] tokens = str.split(regex);
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        return tokens;
    }

    public static Integer getInteger(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public static String getString(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

    public static boolean getBoolean(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return value.toUpperCase().charAt(0) == 'T';
    }
}
