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
		return str.toLowerCase().replaceAll("[^0-9" + other + "]", replacement);
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

	public static String pretify(String str) {
		String result = str;
		if (result != null) {
			result = result.replaceAll("\n", " ");
			result = result.replaceAll("\r", " ");
			result = result.replaceAll("\\.", ". ");
			result = result.replaceAll("&amp;", "&");

			result = result.replaceAll(" : ", ": ");
			result = result.replaceAll(":", ": ");

			result = result.replaceAll(" - ", ": ");
			result = result.replaceAll("- ", ": ");

			while (StringUtils.contains(result, "  ")) {
				result = result.replaceAll("  ", " ");
			}
		}
		return result;
	}
}
