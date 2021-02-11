package com.lazygalaxy.load;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.util.JSONUtil;

public abstract class WikiMediaLoad<T extends MongoDocument> {
	private static final Logger LOGGER = LogManager.getLogger(WikiMediaLoad.class);

	private final MongoHelper<T> helper;

	public WikiMediaLoad(Class<T> clazz) throws Exception {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String title) throws Exception {
		String link = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&rvslots=*&format=json&titles="
				+ title;
		LOGGER.info("loading: " + link);

		JsonObject object = JSONUtil.load(link);

		JsonElement element = object.get("query").getAsJsonObject().get("pages");
		object = element.getAsJsonObject();
		String pageId = object.keySet().toArray()[0].toString();
		object = object.get(pageId).getAsJsonObject();

		title = object.get("title").getAsString();
		object = object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("slots").getAsJsonObject();
		if (object.keySet().size() != 1) {
			throw new Exception("unexpected multiple slots: " + object.keySet());
		}

		String mainContent = object.get("main").getAsJsonObject().get("*").getAsString();

		List<T> documents = getMongoDocuments(mainContent);
		for (T document : documents) {
			helper.upsert(document, true);
			LOGGER.info(document);
		}
	}

	protected abstract List<T> getMongoDocuments(String content) throws Exception;
}
