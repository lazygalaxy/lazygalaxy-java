package com.lazygalaxy.sport.load;

import java.util.Arrays;
import java.util.List;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.sport.domain.Team;

public class TeamCSVLoad extends CSVLoad<Team> {

	private MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public TeamCSVLoad() throws Exception {
		super(Team.class);
	}

	@Override
	protected List<Team> getMongoDocument(String[] tokens) throws Exception {
		Country country = countryHelper.getDocumentsByLabel(tokens[1]).get(0);
		Team team = new Team(tokens[0], Arrays.copyOfRange(tokens, 3, tokens.length), country);
		team.whoScored.id = Integer.parseInt(tokens[2]);

		return null;
	}

}
