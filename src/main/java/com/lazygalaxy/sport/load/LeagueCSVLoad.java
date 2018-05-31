package com.lazygalaxy.sport.load;

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
	protected League getMongoDocument(String[] tokens) throws Exception {
		Country country = countryHelper.getDocumentByLabel(tokens[1]);
		League league = new League(tokens[0], Arrays.copyOfRange(tokens, 3, tokens.length), country,
				Integer.parseInt(tokens[2]));

		return league;
	}

}
