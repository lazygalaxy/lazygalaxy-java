package com.lazygalaxy.game.domain;

import java.util.Set;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";

	public String systemId;
	public String gameId;
	public Set<String> family;
	public GameInfo gameInfo;

	public GameInfo lazygalaxyGameInfo;
	public GameInfo mameGameInfo;
	public GameInfo vman_blissGameInfo;
	public GameInfo rickdangerous_ultimateGameInfo;
	public GameInfo wolfanoz_12kGameInfo;

	public Game() {

	}

	public Game(String systemId, String gameId) throws Exception {
		super(systemId + "_" + gameId, false, null, null);
		this.systemId = systemId;
		this.gameId = gameId;
	}
}
