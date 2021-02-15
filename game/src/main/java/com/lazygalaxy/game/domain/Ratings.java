package com.lazygalaxy.game.domain;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Ratings extends MongoDocument {
	public static final String DATABASE = "game";

	public Double screenScrapperFr;
	public Integer watchMojo;

	public Ratings() {
	}

	public Ratings(String id, Double screenScrapperFr, Integer watchMojo) throws Exception {
		super(id, null, null);
		this.screenScrapperFr = screenScrapperFr;
		this.watchMojo = watchMojo;
	}
}
