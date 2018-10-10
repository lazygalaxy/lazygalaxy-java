package com.lazygalaxy.main.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.world.json.SiteWikipediaJSoupLoad;

public class loadSitesMain {
	private static final Logger LOGGER = LogManager.getLogger(loadSitesMain.class);

	public static void main(String[] args) {
		try {
			new SiteWikipediaJSoupLoad().load("List_of_World_Heritage_Sites_in_Italy");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
