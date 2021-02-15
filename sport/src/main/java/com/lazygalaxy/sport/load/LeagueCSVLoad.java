package com.lazygalaxy.sport.load;

import java.util.Arrays;
import java.util.List;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.sport.domain.League;

public class LeagueCSVLoad extends CSVLoad<League> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public LeagueCSVLoad() throws Exception {
		super(League.class);
	}

	@Override
	protected League getMongoDocument(String[] tokens) throws Exception {
		List<Country> country = countryHelper.getDocumentsByLabel(tokens[1]);
		League league = new League(tokens[0], Arrays.copyOfRange(tokens, 3, tokens.length), country.get(0),
				Integer.parseInt(tokens[2]));

		return league;
	}

}
