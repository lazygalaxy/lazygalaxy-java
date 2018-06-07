package com.lazygalaxy.sport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.helpers.MongoConnectionHelper;
import com.lazygalaxy.sport.load.csv.CountryCSVLoad;
import com.lazygalaxy.sport.load.csv.LeagueCSVLoad;
import com.lazygalaxy.sport.load.csv.TeamCSVLoad;

public class LoadCSVMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadCSVMain.class);

	public static void main(String[] args) {
		try {
			new CountryCSVLoad().load("country.csv");
			new TeamCSVLoad().load("team.csv");
			new LeagueCSVLoad().load("league.csv");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
