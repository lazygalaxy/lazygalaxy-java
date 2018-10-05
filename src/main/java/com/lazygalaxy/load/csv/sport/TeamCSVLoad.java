package com.lazygalaxy.load.csv.sport;

import java.util.Arrays;

import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.domain.world.Country;
import com.lazygalaxy.helpers.MongoHelper;
import com.lazygalaxy.load.csv.CSVLoad;

public class TeamCSVLoad extends CSVLoad<Team> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public TeamCSVLoad() {
		super(Team.class);
	}

	@Override
	protected Team getMongoDocument(String[] tokens) throws Exception {
		Country country = countryHelper.getDocumentByLabel(tokens[1]);
		Team team = new Team(tokens[0], Arrays.copyOfRange(tokens, 3, tokens.length), country);
		team.whoScored.id = Integer.parseInt(tokens[2]);
		return team;
	}

}
