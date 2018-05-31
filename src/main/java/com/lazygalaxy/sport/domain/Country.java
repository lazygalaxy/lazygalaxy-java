package com.lazygalaxy.sport.domain;

public class Country extends MongoDocument {
	public String iso2;
	public String iso3;

	public Country() {
	}

	public Country(String name, String[] labels, String iso2, String iso3) throws Exception {
		super(iso2, name, labels);
		this.iso2 = iso2;
		this.iso3 = iso3;
	}
}
