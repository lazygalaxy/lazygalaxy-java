package com.lazygalaxy.game.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetUtil {

    public static Set<String> addValueToTreeSet(Set<String> valueSet, String... values) {
        if (values != null && values.length > 0) {
            if (valueSet == null) {
                valueSet = new TreeSet<String>();
            }

            for (String value : values) {
                if (!StringUtils.isBlank(value)) {
                    valueSet.add(value);
                }
            }
        }
        if (valueSet.size() > 0) {
            return valueSet;
        }
        return null;
    }

    public static List<String> addValueToArrayList(List<String> valueList, String... values) {
        if (values != null && values.length > 0) {
            if (valueList == null) {
                valueList = new ArrayList<String>();
            }

            for (String value : values) {
                if (!StringUtils.isBlank(value) && !valueList.contains(value)) {
                    valueList.add(value);
                }
            }
        }
        if (valueList.size() > 0) {
            return valueList;
        }
        return null;
    }

    public static boolean contains(Set<String> valueSet, String value) {
        return valueSet != null && valueSet.contains(value);
    }
}
