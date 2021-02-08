package com.lazygalaxy.load;

import java.util.Arrays;

import com.lazygalaxy.domain.Country;
import com.lazygalaxy.domain.League;
import com.lazygalaxy.helper.MongoHelper;

public class LeagueCSVLoad extends CSVLoad<League> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public LeagueCSVLoad() throws Exception {
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
