package com.lazygalaxy.utils;

import org.apache.commons.lang3.StringUtils;

public class GeneralUtil {
	public static String alphanumerify(String str) {
		return StringUtils.stripAccents(str).toLowerCase().replaceAll("[^a-z0-9]", "");
	}

	public static String numerify(String str) {
		return str.toLowerCase().replaceAll("[^0-9]", "");
	}

	public static String alphify(String str) {
		return StringUtils.stripAccents(str).toLowerCase().replaceAll("[^a-z]", "");
	}
}
