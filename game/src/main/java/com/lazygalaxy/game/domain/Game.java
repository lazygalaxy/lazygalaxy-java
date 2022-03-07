package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";
	public static final String[] EMPTY_LIST = new String[0];

	public String romId;
	public String systemId;

	public GameInfo gameInfo;
	public GameInfo vmanGameInfo;
	public GameInfo rickdangerousGameInfo;

	// to do
	public Set<String> romSets;

	public String sourceFile;
	public String cloneOf;
	public Set<String> clones;
	public String romOf;
	public String sampleOf;
	public Boolean isMechanical;

	public String status;
	public Boolean isVeritcal;
	public Integer coins;
	public Set<String> input;
	public Integer buttons;

	public Set<String> collections;

	public Boolean hide;

	public Game() {

	}

	public Game(String romId, String systemId) throws Exception {
		super(systemId + "_" + romId, null, null);
		this.romId = romId;
		this.systemId = systemId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this);
	}
}
