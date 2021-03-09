package com.lazygalaxy.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
	private static final List<String> TRUE_OPTIONS = Arrays.asList("true", "t", "yes", "1");

	private static NodeList getNodes(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			return nodeList;
		}
		return null;
	}

	// attributes
	public static String getAttributeAsString(Node element, String attribute) {
		Node node = element.getAttributes().getNamedItem(attribute);
		if (node != null) {
			String text = node.getTextContent().trim();
			if (!StringUtils.isBlank(text)) {
				return text;
			}
		}
		return null;
	}

	public static Boolean getAttributeAsBoolean(Node element, String attribute) {
		Node node = element.getAttributes().getNamedItem(attribute);
		if (node != null) {
			String text = node.getTextContent().trim().toLowerCase();
			if (!StringUtils.isBlank(text)) {
				return TRUE_OPTIONS.contains(text);
			}
		}
		return null;
	}

	public static Set<String> getTagAttributeAsStringSet(Element element, String tagName, String attribute) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Set<String> result = new TreeSet<String>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				String value = getAttributeAsString(nodeList.item(i), attribute);
				if (!StringUtils.isBlank(value)) {
					result.add(value);
				}
			}
			if (!result.isEmpty()) {
				return result;
			}
		}
		return null;
	}

	public static List<String> getTagAttributeAsString(Element element, String tagName, String attribute) {
		NodeList nodeList = getNodes(element, tagName);
		if (nodeList != null) {
			List<String> result = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				String value = getAttributeAsString(nodeList.item(i), attribute);
				if (!StringUtils.isBlank(value)) {
					result.add(value);
				}
			}
			if (result.size() > 0) {
				return result;
			}
		}
		return null;

	}

	public static String getTagAttributeAsString(Element element, String tagName, String attribute, int index) {
		List<String> list = getTagAttributeAsString(element, tagName, attribute);
		if (list != null) {
			return list.get(index);
		}
		return null;

	}

	public static Integer getTagAttributeAsInteger(Element element, String tagName, String attribute, int index) {
		List<String> list = getTagAttributeAsString(element, tagName, attribute);
		if (list != null) {
			return Integer.parseInt(list.get(index));
		}
		return null;
	}

	// just tags
	public static Boolean containsTag(Element element, String tagName) {
		NodeList nodeList = getNodes(element, tagName);
		if (nodeList != null) {
			return true;
		}
		return false;

	}

	public static List<String> getTagAsString(Element element, String tagName) {
		NodeList nodeList = getNodes(element, tagName);
		if (nodeList != null) {
			List<String> result = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				String value = nodeList.item(i).getTextContent().trim();
				if (!StringUtils.isBlank(value)) {
					result.add(value);
				}
			}
			if (result.size() > 0) {
				return result;
			}
		}
		return null;

	}

	public static String getTagAsString(Element element, String tagName, int index) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			return list.get(index);
		}
		return null;

	}

	public static List<String[]> getTagAsStringArray(Element element, String tagName, String regex) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			List<String[]> result = new ArrayList<>();
			for (String item : list) {
				result.add(GeneralUtil.split(item, regex));
			}
			return result;
		}
		return null;
	}

	public static Set<String> getTagAsStringSet(Element element, String tagName, String regex) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			Set<String> result = new TreeSet<String>();
			for (String listItem : list) {
				for (String tokenItem : GeneralUtil.split(listItem, regex)) {
					result.add(tokenItem);
				}
			}
			return result;
		}
		return null;
	}

	public static Double getTagAsDouble(Element element, String tagName, int index) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			return Double.parseDouble(list.get(index));
		}
		return null;
	}

	public static Integer getTagAsInteger(Element element, String tagName, int index) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			return Integer.parseInt(list.get(index));
		}
		return null;
	}

	public static Boolean getTagAsBoolean(Element element, String tagName, int index) {
		List<String> list = getTagAsString(element, tagName);
		if (list != null) {
			return TRUE_OPTIONS.contains(list.get(index));
		}
		return null;
	}

}
