package com.lazygalaxy.load;

import java.util.Arrays;

import com.lazygalaxy.domain.Country;

public class CountryCSVLoad extends CSVLoad<Country> {

	public CountryCSVLoad() throws Exception {
		super(Country.class);
	}

	@Override
	protected Country getMongoDocument(String[] tokens) throws Exception {
		Country country = new Country(tokens[0], Arrays.copyOfRange(tokens, 4, tokens.length), tokens[1], tokens[2]);
		return country;
	}

}
