package com.lazygalaxy.game.util;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class SetUtil {

	public static Set<String> addValue(Set<String> valueSet, String... values) {
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
		return valueSet;
	}

	public static Set<String> addValue(Set<String> valueSet, Set<String> values) {
		if (values != null && values.size() > 0) {
			if (valueSet == null) {
				valueSet = new TreeSet<String>(values);
			} else {
				for (String value : values) {
					if (!StringUtils.isBlank(value)) {
						valueSet.add(value);
					}
				}
			}
		}
		return valueSet;
	}

	public static boolean contains(Set<String> valueSet, String value) {
		return valueSet != null && valueSet.contains(value);
	}
}
