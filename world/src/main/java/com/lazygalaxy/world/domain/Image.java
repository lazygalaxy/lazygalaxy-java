package com.lazygalaxy.world.domain;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Image extends MongoDocument {
	public String link;

	public Image() {
	}

	public Image(String id, String name, String link) throws Exception {
		super(id, name, null);
		this.link = link;
	}
}
