package com.lazygalaxy.sport.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.sport.load.LeagueCSVLoad;
import com.lazygalaxy.sport.load.TeamCSVLoad;

public class loadCSVMain {
	private static final Logger LOGGER = LogManager.getLogger(loadCSVMain.class);

	public static void main(String[] args) {
		try {
			new TeamCSVLoad().load("csv/team.csv", false);
			new LeagueCSVLoad().load("csv/league.csv", false);
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
