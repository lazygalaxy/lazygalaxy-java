package com.lazygalaxy.game.domain;

import java.util.Set;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";

	public String gameId;
	public String systemId;

	public Set<String> family;
	public Boolean favourite;
	public Boolean hide;

	public GameInfo lazygalaxyGameInfo;
	public GameInfo mameGameInfo;
	// retropie images
	public GameInfo vman_blissGameInfo;
	public GameInfo rickdangerous_ultimateGameInfo;
	public GameInfo wolfanoz_12kGameInfo;
	// coinops
	public GameInfo playerlegends2GameInfo;
	public GameInfo retroarcade2elitesGameInfo;

	public String year;
	public Integer players;
	public String description;
	public String developer;
	public String publisher;

	public Game() {

	}

	public Game(String systemId, String gameId) throws Exception {
		super(systemId + "_" + gameId, false, null, null);
		this.gameId = gameId;
		this.systemId = systemId;
	}
}
