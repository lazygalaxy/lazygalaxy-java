package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";
	public static final String[] EMPTY_LIST = new String[0];

	public String romId;
	public String alternativeId;
	public String systemId;
	public Integer year;
	public String developer;
	public String publisher;
	public String description;

	public Set<String> genre;
	public Set<Integer> players;
	public Set<String> collections;

	public Boolean isVeritcal;
	public Double rating;
	public Boolean hide;

	public Game() {
	}

	public Game(String id, String name) throws Exception {
		super(id, name, EMPTY_LIST);
	}

	public Game(String id, String name, String[] labels) throws Exception {
		super(id, name, labels);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
