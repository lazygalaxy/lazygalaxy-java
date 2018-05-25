package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.load.csv.CountryCSVLoad;
import com.lazygalaxy.sport.load.csv.LeagueCSVLoad;
import com.lazygalaxy.sport.load.csv.TeamCSVLoad;

public class LoadMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadMain.class);

	public static void main(String[] args) {
		try {
			new CountryCSVLoad().load("country.csv");
			new TeamCSVLoad().load("team.csv");
			new LeagueCSVLoad().load("league.csv");
			// new MatchYahooHTMLLoad(leagueHelper.getDocumentByLabel("English
			// Premier League"))
			// .upsert("https://sports.yahoo.com/soccer/premier-league/watford-newcastle-united-80366/");

		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
