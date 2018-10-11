package com.lazygalaxy.main.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.world.json.WikipediaPageJSoupLoad;

public class loadWikipediaMain {
	private static final Logger LOGGER = LogManager.getLogger(loadWikipediaMain.class);

	public static void main(String[] args) {
		try {
			new WikipediaPageJSoupLoad().upsert("List_of_World_Heritage_Sites_in_Italy");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
