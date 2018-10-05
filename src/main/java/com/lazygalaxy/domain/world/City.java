package com.lazygalaxy.domain.world;

import com.lazygalaxy.domain.MongoDocument;

public class City extends MongoDocument {
	public String countryId;

	public City() {
	}

	public City(String name, String[] labels, Country country) throws Exception {
		super(buildId("_", name, country.id), name, labels);
		this.countryId = country.id;
	}
}
