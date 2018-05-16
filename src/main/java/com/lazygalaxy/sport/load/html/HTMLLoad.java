package com.lazygalaxy.sport.load.html;

import java.io.File;
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

		if (link.contains("http://")) {
			document = Jsoup.connect(link).get();
		} else {
			File file = new File(HTMLLoad.class.getClassLoader().getResource(link).getFile());
			document = Jsoup.parse(file, "UTF-8");
		}

		return document;
	}

	public void load(String html) throws Exception {
		Set<String> linkSet = getLinks(html);
		for (String documentHTML : linkSet) {
			T document = getMongoDocument(documentHTML);
			helper.upsertByLabel(document);
		}
	}

	public abstract Set<String> getLinks(String html) throws Exception;

	public abstract T getMongoDocument(String html) throws Exception;
}