package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.game.util.SetUtil;

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
	public Set<String> manufacturers;
	public Set<String> emulators;

	public GameInfo() {

	}

	public GameInfo(String path, String name, String year, String description, String genre, String image, String video,
			String marquee, Double rating, Integer players, Set<String> manufacturers, Set<String> emulators) {
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
		this.manufacturers = SetUtil.addValue(this.manufacturers, manufacturers);
		this.emulators = SetUtil.addValue(this.emulators, emulators);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
