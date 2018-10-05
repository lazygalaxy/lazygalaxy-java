package com.lazygalaxy.load.csv.world;

import java.util.Arrays;

import com.lazygalaxy.domain.world.Country;
import com.lazygalaxy.load.csv.CSVLoad;

public class CountryCSVLoad extends CSVLoad<Country> {

	public CountryCSVLoad() {
		super(Country.class);
	}

	@Override
	protected Country getMongoDocument(String[] tokens) throws Exception {
		Country country = new Country(tokens[0], Arrays.copyOfRange(tokens, 4, tokens.length), tokens[1], tokens[2]);
		return country;
	}

}
