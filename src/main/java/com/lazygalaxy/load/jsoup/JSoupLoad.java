package com.lazygalaxy.load.jsoup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.helpers.MongoHelper;

public abstract class JSoupLoad<T extends MongoDocument> {
	private static final Logger LOGGER = LogManager.getLogger(JSoupLoad.class);

	protected final MongoHelper<T> helper;

	public JSoupLoad(Class<T> clazz) {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void upsert(String link) throws Exception {
		Document htmlDocument = getHTMLDocument(link);
		List<T> documents = getMongoDocuments(htmlDocument);
		for (T document : documents) {
			helper.upsert(document);
		}
	}

	public void print(String link) throws Exception {
		Document htmlDocument = getHTMLDocument(link);
		List<T> documents = getMongoDocuments(htmlDocument);
		for (T document : documents) {
			LOGGER.info("printing: " + document);
		}
	}

	public void saveHTML(String link) throws Exception {
		Document htmlDocument = getHTMLDocument(link);
		List<T> mongoDocument = getMongoDocuments(htmlDocument);

		Path path = Paths.get(mongoDocument.get(0).name + ".html");
		Files.write(path, htmlDocument.html().getBytes(), StandardOpenOption.CREATE_NEW);
	}

	public Document getHTMLDocument(String link) throws Exception {
		Document document = null;

		if (link.startsWith("http")) {
			document = Jsoup.connect(link).get();
		} else {
			File file = new File(JSoupLoad.class.getClassLoader().getResource(link).getFile());
			document = Jsoup.parse(file, "UTF-8");
		}

		return document;
	}

	protected abstract List<T> getMongoDocuments(Document htmlDocument) throws Exception;
}