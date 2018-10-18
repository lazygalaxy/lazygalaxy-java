package com.lazygalaxy.utils;

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
		return str.toLowerCase().replaceAll("[^0-9" + other + "]", replacement);
	}

	public static String alphify(String str) {
		return StringUtils.stripAccents(str).toLowerCase().replaceAll("[^a-z]", "");
	}
}
