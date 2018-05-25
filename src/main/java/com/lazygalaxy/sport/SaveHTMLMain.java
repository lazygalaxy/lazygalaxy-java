package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.html.MatchYahooHTMLLoad;

public class SaveHTMLMain {
	private static final Logger LOGGER = LogManager.getLogger(SaveHTMLMain.class);

	public static void main(String[] args) {
		try {
			MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);

			new MatchYahooHTMLLoad(leagueHelper.getDocumentByLabel("English Premier League"))
					.saveHTML("https://sports.yahoo.com/soccer/premier-league/watford-newcastle-united-80366/");

		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
