package com.lazygalaxy.game.domain;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Scores extends MongoDocument {
	public static final String DATABASE = "game";

	public Integer vman;
	public Integer rickDangerous;
	public Integer watchMojo;
	public Integer antopisa;
	public Integer lazygalaxy;

	public Scores() {
	}

	public Scores(String id) throws Exception {
		super(id, null, null);
	}
}
