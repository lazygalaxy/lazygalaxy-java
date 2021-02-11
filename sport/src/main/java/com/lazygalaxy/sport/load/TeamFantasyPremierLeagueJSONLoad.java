package com.lazygalaxy.sport.load;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.JSONLoad;
import com.lazygalaxy.sport.domain.Team;

public class TeamFantasyPremierLeagueJSONLoad extends JSONLoad<Team> {
	private static final Logger LOGGER = LogManager.getLogger(TeamFantasyPremierLeagueJSONLoad.class);

	public TeamFantasyPremierLeagueJSONLoad() throws Exception {
		super(Team.class);
	}

	@Override
	protected JsonArray getJsonArray(JsonObject object) throws Exception {
		return object.getAsJsonArray("teams");
	}

	@Override
	protected Team getMongoDocument(JsonObject object) throws Exception {
		Country ENGLAND = MongoHelper.getHelper(Country.class).getDocumentById("en");

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
