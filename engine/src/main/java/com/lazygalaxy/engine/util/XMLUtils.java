package com.lazygalaxy.engine.util;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils {

	public static String handleString(Element element, String tagName) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return nodes.item(0).getTextContent().trim();
		}
		return null;
	}

	public static Double handleDouble(Element element, String tagName) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return Double.parseDouble(nodes.item(0).getTextContent().trim());
		}
		return null;
	}

	public static Integer handleInteger(Element element, String tagName, int length) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return Integer.parseInt(StringUtils.left(nodes.item(0).getTextContent().trim(), length));
		}
		return null;
	}

}
