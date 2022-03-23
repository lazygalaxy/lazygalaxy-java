package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.util.GeneralUtil;

public class GameInfo {
	// retroarch
	public String path;
	public String originalName;
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
	public String emulator;

	// derived
	public String name;
	public String version;

	// mame
	public Boolean isVeritcal;
	public Set<String> inputs;
	public Integer buttons;
	public String status;
	public Boolean isGuess;

	public GameInfo() {

	}

	// retroarch constructor
	public GameInfo(String path, String originalName, String year, String description, String genre, String image,
			String video, String marquee, Double rating, String players, String developer, String publisher,
			String emulator) {
		this(path, originalName, year, description, genre, image, video, marquee, rating, players, developer, publisher,
				emulator, null, null, null, null, null);
	}

	// mame constructor
	public GameInfo(String originalName, String year, String players, String developer, Boolean isVertical,
			Set<String> inputs, Integer buttons, String status, Boolean isGuess) {
		this(null, originalName, year, null, null, null, null, null, null, players, developer, null, null, isVertical,
				inputs, buttons, status, isGuess);
	}

	// general constructor
	public GameInfo(String path, String originalName, String year, String description, String genre, String image,
			String video, String marquee, Double rating, String players, String developer, String publisher,
			String emulator, Boolean isVeritcal, Set<String> inputs, Integer buttons, String status, Boolean isGuess) {
		this.path = path;
		this.originalName = originalName;
		if (year != null && !StringUtils.equals(year, "1970")) {
			this.year = StringUtils.left(year, 4);
			if (StringUtils.contains(this.year, "?")) {
				this.year = null;
			}
		}
		this.description = description;
		this.genre = genre;
		this.image = image;
		this.video = video;
		this.marquee = marquee;
		this.rating = rating;
		if (rating != null && rating <= 0) {
			this.rating = null;
		}
		if (!StringUtils.isBlank(players)) {
			String[] playerArray = players.split("-");
			this.players = Integer.parseInt(GeneralUtil.numerify(playerArray[playerArray.length - 1]));
		}
		this.developer = developer;
		if (StringUtils.equals(developer, "Data East USA")) {
			this.developer = "Data East";
		}

		this.publisher = publisher;
		if (StringUtils.equals(publisher, "Data East USA")) {
			this.publisher = "Data East";
		}
		if (StringUtils.equals(developer, publisher)) {
			this.publisher = null;
		}

		this.emulator = emulator;

		if (isVeritcal != null && isVeritcal) {
			this.isVeritcal = isVeritcal;
		}

		this.inputs = inputs;

		this.buttons = buttons;

		this.status = status;

		if (isGuess != null && isGuess) {
			this.isGuess = isGuess;
		}
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
