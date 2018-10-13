package com.lazygalaxy.domain.world;

import com.lazygalaxy.domain.MongoDocument;

public class Image extends MongoDocument {
	public String link;
	public String caption;

	public Image() {
	}

	public Image(String id, String caption, String link) throws Exception {
		super(id, caption, null);
		this.link = link;
		this.caption = caption;
	}
}
