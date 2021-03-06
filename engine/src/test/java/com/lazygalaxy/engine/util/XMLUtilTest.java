package com.lazygalaxy.engine.util;

import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

public class XMLUtilTest extends TestCase {
	public void testTagAttributeAsStringSet() throws Exception {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document xmlDocument = dBuilder
				.parse(Paths.get(ClassLoader.getSystemResource("xml/mame.xml").toURI()).toFile());

		NodeList nodes = xmlDocument.getElementsByTagName("machine");

		Element element1 = (Element) nodes.item(0);
		Set<String> set1 = XMLUtil.getTagAttributeAsStringSet(element1, "control", "type");
		String[] array1 = set1.toArray(new String[set1.size()]);

		assertEquals(2, array1.length);
		assertEquals("dial", array1[0]);
		assertEquals("pedal", array1[1]);

		Element element2 = (Element) nodes.item(1);
		List<String> set2 = XMLUtil.getTagAsString(element2, "year");
		assertNull(set2);
	}
}
