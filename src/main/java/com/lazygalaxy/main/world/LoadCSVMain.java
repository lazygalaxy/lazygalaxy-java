package com.lazygalaxy.main.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helpers.MongoConnectionHelper;
import com.lazygalaxy.load.world.CountryCSVLoad;

public class LoadCSVMain {
	private static final Logger LOGGER = LogManager.getLogger(LoadCSVMain.class);

	public static void main(String[] args) {
		try {
			new CountryCSVLoad().load("csv/country.csv");
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
