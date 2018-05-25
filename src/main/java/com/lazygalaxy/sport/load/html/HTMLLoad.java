package com.lazygalaxy.sport.load.html;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.lazygalaxy.sport.domain.MongoDocument;
import com.lazygalaxy.sport.helpers.MongoHelper;

public abstract class HTMLLoad<T extends MongoDocument> {
	private final MongoHelper<T> helper;

	public HTMLLoad(Class<T> clazz) {
		this.helper = MongoHelper.getHelper(clazz);
	}

	protected Document getHTMLDocument(String link) throws Exception {
		Document document = null;

		if (link.startsWith("http")) {
			document = Jsoup.connect(link).get();
		} else {
			File file = new File(HTMLLoad.class.getClassLoader().getResource(link).getFile());
			document = Jsoup.parse(file, "UTF-8");
		}

		return document;
	}

	public void load(String link) throws Exception {
		Set<String> linkSet = getLinks(link);
		for (String documentLink : linkSet) {
			upsert(documentLink);
		}
	}

	public void upsert(String link) throws Exception {
		T document = getMongoDocument(link);
		helper.upsertByLabel(document);
	}

	public void saveHTML(String link) throws Exception {
		Document document = getHTMLDocument(link);
		MongoDocument mongoDocument = getMongoDocument(link);

		Path path = Paths.get(mongoDocument.getName() + ".html");
		Files.write(path, document.html().getBytes(), StandardOpenOption.CREATE_NEW);
	}

	public abstract Set<String> getLinks(String html) throws Exception;

	public abstract T getMongoDocument(String html) throws Exception;
}