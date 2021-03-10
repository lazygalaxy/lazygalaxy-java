package com.lazygalaxy.game.domain;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.util.GeneralUtil;

public class Game extends MongoDocument {
	public static final String DATABASE = "game";
	public static final String[] EMPTY_LIST = new String[0];

	// master mame file
	public String rom;
	public String sourceFile;
	public Set<String> clones;
	public String romOf;
	public Boolean isMechanical;
	public Set<String> extraInfo;

	public String year;
	public String status;
	public Set<String> manufacturers;
	public Boolean isVeritcal;
	public Integer players;
	public Integer coins;
	public Set<String> input;
	public Integer buttons;

	// derived from master mame file
	public String systemId;

	// enriched from other sources
	public String description;
	public Set<String> genre;
	public Set<String> collections;
	public String genreLabel;
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

	public void addGenre(String genre) {
		this.genre.add(GeneralUtil.alphanumerify(genre));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
