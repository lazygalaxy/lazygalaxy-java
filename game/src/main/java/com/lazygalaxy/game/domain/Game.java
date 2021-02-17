package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";

	public String system;
	public String path;
	public String description;
	public Double rating;
	public Integer year;
	public String developer;
	public String publisher;
	public String genre;
	public Set<Integer> players;
	public Boolean hide;

	public Game() {
	}

	public Game(String id, String name, String[] labels, String system, String path, String description, Double rating,
			Integer year, String developer, String publisher, String genre, Set<Integer> players, Boolean hide)
			throws Exception {
		super(id, name, labels);
		this.system = system;
		this.path = path;
		this.description = description;
		this.rating = rating;
		this.year = year;
		this.developer = developer;
		this.publisher = publisher;
		this.genre = genre;
		this.players = players;
		this.hide = hide;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
