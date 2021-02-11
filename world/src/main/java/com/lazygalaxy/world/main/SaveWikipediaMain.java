package com.lazygalaxy.world.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.world.load.wikipedia.WHSParentProcessor;
import com.lazygalaxy.world.load.wikipedia.WikipediaPageJSoupLoad;

public class SaveWikipediaMain {
	private static final Logger LOGGER = LogManager.getLogger(UpsertWHSWikipediaMain.class);

	public static void main(String[] args) {
		try {
			WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad(new WHSParentProcessor());
			loader.saveHTML("World_Heritage_sites_by_country");

			loader.saveHTML("List_of_World_Heritage_Sites_in_Italy");
			loader.saveHTML("List_of_World_Heritage_sites_in_Belarus");

			loader.saveHTML("Historic_Centre_of_Florence");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
