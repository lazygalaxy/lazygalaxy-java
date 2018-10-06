package com.lazygalaxy.domain.world;

import com.lazygalaxy.domain.MongoDocument;

public class Site extends MongoDocument {
	public String coordinates;

	public Site() {
	}

	public Site(String name, String[] labels, String coordinates) throws Exception {
		super(buildId("_", name), name, labels);
		this.coordinates = coordinates;
	}
}
