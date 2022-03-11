package com.lazygalaxy.game.domain;

import java.util.Set;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";
	public static final String[] EMPTY_LIST = new String[0];

	public String romId;
	public String systemId;

	public String year;
	public Integer players;
	public String description;

	public GameInfo mameGameInfo;
	public GameInfo vmanGameInfo;
	public GameInfo rickdangerousGameInfo;
	public GameInfo wolfanozGameInfo;

	// mame xml
	public String cloneOfRomId;
	public String sourceFile;
	public String sampleOf;
	public String status;
	public Boolean isVeritcal;
	public Integer coins;
	public Set<String> input;
	public Integer buttons;
	public Set<String> clones;
	public Boolean parentMissing;

	// custom
	public Set<String> collections;
	public Boolean hide;

	public Game() {

	}

	public Game(String romId, String systemId) throws Exception {
		super(systemId + "_" + romId, null, null);
		this.romId = romId;
		this.systemId = systemId;
	}
}
