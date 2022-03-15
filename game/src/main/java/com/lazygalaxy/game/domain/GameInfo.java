package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
	public String developer;
	public String publisher;
	public Set<String> emulators;

	public GameInfo() {

	}

	public GameInfo(String path, String name, String year, String description, String genre, String image, String video,
			String marquee, Double rating, Integer players, String developer, String publisher, String emulator) {
		this.path = path;
		this.name = name;

		if (year != null && !StringUtils.equals(year, "1970")) {
			this.year = StringUtils.left(year, 4);
		}

		this.description = description;
		this.genre = genre;
		this.image = image;
		this.video = video;
		this.marquee = marquee;
		this.rating = rating;
		this.players = players;

		if (StringUtils.equals(developer, "Data East USA")) {
			developer = "Data East";
		}
		if (StringUtils.equals(publisher, "Data East USA")) {
			publisher = "Data East";
		}
		this.developer = developer;
		this.publisher = publisher;
		if (StringUtils.equals(developer, publisher)) {
			this.publisher = null;
		}

		this.emulators = SetUtil.addValue(this.emulators, emulator);
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
