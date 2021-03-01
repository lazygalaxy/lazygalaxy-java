package com.lazygalaxy.common.main;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.CSVLoad;

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

	private static class CountryCSVLoad extends CSVLoad<Country> {

		public CountryCSVLoad() throws Exception {
			super(Country.class);
		}

		@Override
		protected List<Country> getMongoDocument(String[] tokens) throws Exception {
			return Arrays
					.asList(new Country(tokens[0], Arrays.copyOfRange(tokens, 4, tokens.length), tokens[1], tokens[2]));
		}

	}
}
