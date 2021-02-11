package com.lazygalaxy.game.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";
	public String system;
	public String path;
	public String description;
	public Double rating;
	public Integer date;
	public String developer;
	public String publisher;
	public String genre;
	public String players;

	public Game() {
	}

	public Game(String name, String[] labels, String system, String path, String description, Double rating,
			Integer date, String developer, String publisher, String genre, String players) throws Exception {
		super(null, name, labels);
		this.system = system;
		this.path = path;
		this.description = description;
		this.rating = rating;
		this.date = date;
		this.developer = developer;
		this.publisher = publisher;
		this.genre = genre;
		this.players = players;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
