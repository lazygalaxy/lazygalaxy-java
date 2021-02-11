package com.lazygalaxy.engine.load;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.util.JSONUtil;

public abstract class JSONLoad<T extends MongoDocument> {
	private static final Logger LOGGER = LogManager.getLogger(JSONLoad.class);

	private final MongoHelper<T> helper;

	public JSONLoad(Class<T> clazz) throws Exception {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String link) throws Exception {
		JsonObject json = JSONUtil.load(link);

		JsonArray array = getJsonArray(json);
		if (array == null || array.size() == 0) {
			throw new Exception("null or empty array returned");
		}
		for (JsonElement element : array) {
			T document = getMongoDocument(element.getAsJsonObject());
			LOGGER.info(document);
			// TODO: no upsert taking place
		}
	}

	protected abstract JsonArray getJsonArray(JsonObject object) throws Exception;

	protected abstract T getMongoDocument(JsonObject object) throws Exception;
}
