package com.lazygalaxy.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helper.MongoConnectionHelper;
import com.lazygalaxy.load.TeamFantasyPremierLeagueJSONLoad;

public class loadJSONMain {
	private static final Logger LOGGER = LogManager.getLogger(loadJSONMain.class);

	public static void main(String[] args) {
		try {
			new TeamFantasyPremierLeagueJSONLoad().load("json/fantasy-premierleague-static.json");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
