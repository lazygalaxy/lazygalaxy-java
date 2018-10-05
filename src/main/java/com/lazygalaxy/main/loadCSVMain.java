package com.lazygalaxy.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.csv.sport.LeagueCSVLoad;
import com.lazygalaxy.load.csv.sport.TeamCSVLoad;
import com.lazygalaxy.load.csv.world.CountryCSVLoad;

public class loadCSVMain {
	private static final Logger LOGGER = LogManager.getLogger(loadCSVMain.class);

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
