package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.load.jsoup.MatchWhoScoredJSoupLoad;

public class LoadHTMLMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadHTMLMain.class);

	public static void main(String[] args) {
		try {
			MatchWhoScoredJSoupLoad scraper = new MatchWhoScoredJSoupLoad();
			scraper.load("html/whoscored-football-201708-fixtures.html");
			// scraper.getMongoDocument("html/whoscored-manchestercity-vs-stoke-20181017.html");
			// Match match =
			// scraper.getMongoDocument("html/whoscored-watford-vs-newcastle-20180505.html");
			// scraper.getMongoDocument("html/whoscored-leicester-vs-arsenal-20180509.html");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
