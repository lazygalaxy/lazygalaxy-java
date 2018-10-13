package com.lazygalaxy.domain.world;

import java.time.LocalDateTime;

import com.lazygalaxy.domain.MongoDocument;
import com.mongodb.client.model.geojson.Point;

public class WikipediaPage extends MongoDocument {
	public String summary;
	public Point coordinates;
	public Image image;
	public String location;
	public LocalDateTime updateTime;

	public WikipediaPage() {
	}

	public WikipediaPage(String name, String[] labels) throws Exception {
		super(name, name, labels);
	}
}
