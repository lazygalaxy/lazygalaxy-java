package com.lazygalaxy.load;

import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.helper.MongoHelper;

public abstract class XMLLoad<T extends MongoDocument> {

	private final MongoHelper<T> helper;

	public XMLLoad(Class<T> clazz) throws Exception {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String file, String tagName, boolean merge) throws Exception {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document xmlDocument = dBuilder.parse(Paths.get(ClassLoader.getSystemResource(file).toURI()).toFile());
		NodeList nodes = xmlDocument.getElementsByTagName(tagName);
		for (int i = 0; i < nodes.getLength(); i++) {
			T document = getMongoDocument((Element) nodes.item(i));
			helper.upsert(document, merge);
		}
	}

	protected abstract T getMongoDocument(Element element) throws Exception;
}
