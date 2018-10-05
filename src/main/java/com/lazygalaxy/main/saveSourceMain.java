package com.lazygalaxy.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.selenium.sport.MatchWhoScoredSeleniumLoad;

public class saveSourceMain {
	private static final Logger LOGGER = LogManager.getLogger(saveSourceMain.class);

	public static void main(String[] args) {
		try {
			MatchWhoScoredSeleniumLoad load = new MatchWhoScoredSeleniumLoad();
			load.saveSourceLinks("src/main/resources/html/match/",
					"https://www.whoscored.com/Regions/252/Tournaments/2/Seasons/6829/Stages/15151/Fixtures/England-Premier-League-2017-2018",
					9999);
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
