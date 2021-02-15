package com.lazygalaxy.engine.load;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;

public abstract class XMLLoad<T extends MongoDocument> {
	private final MongoHelper<T> helper;

	public XMLLoad(Class<T> clazz) throws Exception {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String file, String gameTagName, String... extraTagNames) throws Exception {
		load(file, gameTagName, null, extraTagNames);
	}

	public void load(String file, String gameTagName, Merge<T> merge, String... extraTagNames) throws Exception {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document xmlDocument = dBuilder.parse(Paths.get(ClassLoader.getSystemResource(file).toURI()).toFile());
		List<String> extraTagValues = new ArrayList<String>();
		for (String tagName : extraTagNames) {
			extraTagValues.add(xmlDocument.getElementsByTagName(tagName).item(0).getTextContent());
		}

		NodeList nodes = xmlDocument.getElementsByTagName(gameTagName);
		for (int i = 0; i < nodes.getLength(); i++) {
			T document = getMongoDocument((Element) nodes.item(i), extraTagValues);
			helper.upsert(document, merge);
		}
	}

	protected abstract T getMongoDocument(Element element, List<String> extraTagValues) throws Exception;
}
