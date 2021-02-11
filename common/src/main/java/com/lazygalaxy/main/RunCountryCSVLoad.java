package com.lazygalaxy.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.helper.MongoConnectionHelper;
import com.lazygalaxy.load.CountryCSVLoad;

public class RunCountryCSVLoad {
	private static final Logger LOGGER = LogManager.getLogger(RunCountryCSVLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new CountryCSVLoad().load("csv/country.csv", false);
			LOGGER.info("csv load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
