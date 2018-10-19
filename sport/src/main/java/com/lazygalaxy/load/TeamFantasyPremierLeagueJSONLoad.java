package com.lazygalaxy.load;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lazygalaxy.domain.Country;
import com.lazygalaxy.domain.Team;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.load.JSONLoad;

public class TeamFantasyPremierLeagueJSONLoad extends JSONLoad<Team> {
	private static final Logger LOGGER = LogManager.getLogger(TeamFantasyPremierLeagueJSONLoad.class);
	private static final Country ENGLAND = MongoHelper.getHelper(Country.class).getDocumentById("en");

	public TeamFantasyPremierLeagueJSONLoad() {
		super(Team.class);
	}

	@Override
	protected JsonArray getJsonArray(JsonObject object) throws Exception {
		return object.getAsJsonArray("teams");
	}

	@Override
	protected Team getMongoDocument(JsonObject object) throws Exception {
		LOGGER.info(object);
		String name = object.get("name").getAsString();
		String shortName = object.get("short_name").getAsString();
		Integer code = object.get("code").getAsInt();

		Team team = new Team(name, new String[] { shortName }, ENGLAND);
		team.shortName = shortName;
		team.fantasyPremierLeague.code = code;
		return team;
	}

}
