package com.lazygalaxy.common.load;

import java.util.Arrays;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.load.CSVLoad;

public class CountryCSVLoad extends CSVLoad<Country> {

	public CountryCSVLoad() throws Exception {
		super(Country.class);
	}

	@Override
	protected Country getMongoDocument(String[] tokens) throws Exception {
		return new Country(tokens[0], Arrays.copyOfRange(tokens, 4, tokens.length), tokens[1], tokens[2]);
	}

}
