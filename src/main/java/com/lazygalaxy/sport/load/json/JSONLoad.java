package com.lazygalaxy.sport.load.json;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lazygalaxy.sport.domain.MongoDocument;
import com.lazygalaxy.sport.helpers.MongoHelper;

public abstract class JSONLoad<T extends MongoDocument> {
	private static final Logger LOGGER = LogManager.getLogger(JSONLoad.class);

	private final MongoHelper<T> helper;

	public JSONLoad(Class<T> clazz) {
		this.helper = MongoHelper.getHelper(clazz);
	}

	protected JsonObject getData(String json) {
		JsonElement element = new JsonParser().parse(json);
		JsonObject object = element.getAsJsonObject();
		return object;
	}

	public void load(String file) throws Exception {
		Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());
		byte[] bytes = Files.readAllBytes(path);

		JsonArray array = getJsonArray(getData(new String(bytes)));
		if (array == null) {
			throw new Exception("null array returned");
		}
		for (JsonElement element : array) {
			T document = getMongoDocument(element.getAsJsonObject());
			LOGGER.info(document);
		}
	}

	protected abstract JsonArray getJsonArray(JsonObject object) throws Exception;

	protected abstract T getMongoDocument(JsonObject object) throws Exception;
}
