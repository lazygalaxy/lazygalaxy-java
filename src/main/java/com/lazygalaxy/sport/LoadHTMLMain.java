package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.load.MatchYahooHTMLLoad;

public class LoadHTMLMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadHTMLMain.class);

	public static void main(String[] args) {
		try {
			new MatchYahooHTMLLoad()
					.saveHTML("https://sports.yahoo.com/soccer/premier-league/watford-newcastle-united-80366/");

		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
