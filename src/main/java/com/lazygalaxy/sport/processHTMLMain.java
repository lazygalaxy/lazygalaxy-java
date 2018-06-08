package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.load.selenium.MatchWhoScoredSeleniumLoad;

public class processHTMLMain {
	private static final Logger LOGGER = LogManager.getLogger(processHTMLMain.class);

	public static void main(String[] args) {
		try {
			MatchWhoScoredSeleniumLoad load = new MatchWhoScoredSeleniumLoad();
			load.saveSource("src/main/resources/html/match/",
					"https://www.whoscored.com/Matches/1190467/LiveStatistics/England-Premier-League-2017-2018-Watford-Newcastle-United");
			// load.upsertLinks(
			// "https://www.whoscored.com/Regions/252/Tournaments/2/Seasons/6829/Stages/15151/Fixtures/England-Premier-League-2017-2018");

		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
