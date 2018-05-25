package com.lazygalaxy.sport.load.csv;

import java.util.Arrays;

import com.lazygalaxy.sport.domain.Country;
import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.helpers.MongoHelper;

public class LeagueCSVLoad extends CSVLoad<League> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public LeagueCSVLoad() {
		super(League.class);
	}

	@Override
	protected League getMongoDocument(String[] tokens) {
		Country country = countryHelper.getDocumentByLabel(tokens[2]);
		League league = new League(tokens[0], tokens[1], Arrays.copyOfRange(tokens, 5, tokens.length), country,
				tokens[3], tokens[4], tokens[5]);

		return league;
	}

}
