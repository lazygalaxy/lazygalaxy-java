package com.lazygalaxy.engine.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils {

	private static final Logger LOGGER = LogManager.getLogger(XMLUtils.class);

	public static String getValue(Document document, String tag) {

		try {
			if (tag.contains(">")) {
				String[] keyArray = tag.split(">");
				NodeList nodes = document.getElementsByTagName(keyArray[0]);
				if (nodes != null) {
					for (int j = 0; j < nodes.getLength(); j++) {
						if (StringUtils.equals(((Element) nodes.item(j)).getTextContent(), keyArray[1])) {
							Element parent = (Element) nodes.item(j).getParentNode();
							Element element = ((Element) parent.getElementsByTagName(keyArray[2]).item(0));
							if (element == null) {
								return null;
							}
							return element.getTextContent();
						}
					}
				}
				return null;
			}

			return XMLUtils.getActualValue(document, tag);
		} catch (Exception e) {
			LOGGER.error("could not retrieve from xml key: " + tag, e);
			return null;
		}
	}

	private static String getActualValue(Document document, String tag) {

		NodeList nodeList = document.getElementsByTagName(tag);
		if (nodeList != null) {
			Element element = ((Element) nodeList.item(0));
			if (element != null) {
				String text = ((Element) document.getElementsByTagName(tag).item(0)).getTextContent();
				if (text != null) {
					return text;
				}
			}
		}
		return null;
	}

}
