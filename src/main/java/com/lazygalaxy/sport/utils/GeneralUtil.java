package com.lazygalaxy.sport.utils;

public class GeneralUtil {
	public static String normalize(String str) {
		return str.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
	}
}
