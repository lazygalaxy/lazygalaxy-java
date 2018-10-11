package com.lazygalaxy.domain.world;

import com.lazygalaxy.domain.MongoDocument;

public class WikipediaPage extends MongoDocument {
	public String summary;
	public String coordinates;

	public WikipediaPage() {
	}

	public WikipediaPage(String name, String[] labels) throws Exception {
		super(name, name, labels);
	}
}
