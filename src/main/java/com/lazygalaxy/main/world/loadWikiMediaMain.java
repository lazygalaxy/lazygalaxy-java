package com.lazygalaxy.main.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.world.SiteWikiMediaLoad;

public class loadWikiMediaMain {
	private static final Logger LOGGER = LogManager.getLogger(loadWikiMediaMain.class);

	public static void main(String[] args) {
		try {
			new SiteWikiMediaLoad().load("List_of_World_Heritage_Sites_in_Greece");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
