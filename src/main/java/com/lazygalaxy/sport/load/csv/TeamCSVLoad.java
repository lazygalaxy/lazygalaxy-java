package com.lazygalaxy.sport.load.csv;

import java.util.Arrays;

import com.lazygalaxy.sport.domain.Country;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;

public class TeamCSVLoad extends CSVLoad<Team> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public TeamCSVLoad() {
		super(Team.class);
	}

	@Override
	protected Team getMongoDocument(String[] tokens) {
		Country country = countryHelper.getDocumentByLabel(tokens[1]);
		Team team = new Team(tokens[0], Arrays.copyOfRange(tokens, 2, tokens.length), country);

		return team;
	}

}
