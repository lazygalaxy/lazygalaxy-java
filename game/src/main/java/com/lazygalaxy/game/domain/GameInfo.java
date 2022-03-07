package com.lazygalaxy.game.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GameInfo {

	public String path;
	public String name;
	public String year;
	public String description;
	public String genre;
	public String image;
	public String video;
	public String marquee;
	public Double rating;
	public Integer players;
	public String developer;
	public String publisher;

	public GameInfo() {

	}

	public GameInfo(String path, String name, String year, String description, String genre, String image, String video,
			String marquee, Double rating, Integer players, String developer, String publisher) {
		this.path = path;
		this.name = name;
		this.year = year;
		this.description = description;
		this.genre = genre;
		this.image = image;
		this.video = video;
		this.marquee = marquee;
		this.rating = rating;
		this.players = players;
		this.developer = developer;
		this.publisher = publisher;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
