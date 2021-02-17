package com.lazygalaxy.engine.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils {
	private static final List<String> TRUE_OPTIONS = Arrays.asList("true", "t", "yes");

	public static String handleString(Element element, String tagName) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0) {
			String text = nodes.item(0).getTextContent().trim();
			if (!StringUtils.isBlank(text))
				return text;
		}
		return null;

	}

	public static String[] handleStringArray(Element element, String tagName, String regexSplitter) {
		String text = handleString(element, tagName);
		if (text != null) {
			String[] tokens = text.split(regexSplitter);
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}
			return tokens;
		}
		return null;
	}

	public static Double handleDouble(Element element, String tagName) {
		String text = handleString(element, tagName);
		if (text != null) {
			return Double.parseDouble(text);
		}
		return null;
	}

	public static Integer handleInteger(Element element, String tagName, int length) {
		String text = handleString(element, tagName);
		if (text != null) {
			return Integer.parseInt(StringUtils.left(text, length));
		}
		return null;
	}

	public static Boolean handleBoolean(Element element, String tagName) {
		String text = handleString(element, tagName);
		if (text != null) {
			return TRUE_OPTIONS.contains(text);
		}
		return null;
	}

}
