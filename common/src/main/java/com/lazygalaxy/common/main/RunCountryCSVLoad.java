package com.lazygalaxy.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.common.load.CountryCSVLoad;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;

public class RunCountryCSVLoad {
	private static final Logger LOGGER = LogManager.getLogger(RunCountryCSVLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new CountryCSVLoad().load("csv/country.csv");
			LOGGER.info("csv load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
