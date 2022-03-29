package com.lazygalaxy.game.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

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

	public static Set<String> addValueToLinkedHashSet(Set<String> valueSet, String... values) {
		if (values != null && values.length > 0) {
			if (valueSet == null) {
				valueSet = new LinkedHashSet<String>();
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

	public static boolean contains(Set<String> valueSet, String value) {
		return valueSet != null && valueSet.contains(value);
	}
}
