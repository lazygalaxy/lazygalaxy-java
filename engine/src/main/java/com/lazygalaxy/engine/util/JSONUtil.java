package com.lazygalaxy.engine.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONUtil {
	private static final Logger LOGGER = LogManager.getLogger(JSONUtil.class);

	public static JsonObject load(String link) throws Exception {
		JsonObject json = null;
		if (link.startsWith("http")) {
			URL url = new URL(link);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");

			int respCode = conn.getResponseCode();
			if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				json = getData(reader);
				reader.close();
			} else {
				LOGGER.error(conn.getResponseCode() + " " + conn.getResponseMessage());
			}

		} else {
			Path path = Paths.get(ClassLoader.getSystemResource(link).toURI());
			byte[] bytes = Files.readAllBytes(path);
			json = getData(new String(bytes));
		}

		return json;
	}

	private static JsonObject getData(String json) {
		JsonElement element = new JsonParser().parse(json);
		JsonObject object = element.getAsJsonObject();
		return object;
	}

	private static JsonObject getData(Reader reader) {
		JsonElement element = new JsonParser().parse(reader);
		JsonObject object = element.getAsJsonObject();
		return object;
	}
}
