package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.html.MatchLiveScoreHTMLLoad;

public class LoadMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadMain.class);

	public static void main(String[] args) {
		try {
			MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);

			// new CountryCSVLoad().load("country.csv");
			// new TeamCSVFLoad().load("team.csv");
			// new LeagueCSVLoad().load("league.csv");
			new MatchLiveScoreHTMLLoad(leagueHelper.getDocumentByLabel("English Premier League"))
					.load("http://www.livescore.com/soccer/2018-05-05/");

		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
