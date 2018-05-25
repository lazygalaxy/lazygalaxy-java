package com.lazygalaxy.sport.domain;

public class Country extends MongoDocument {
	private String iso2;
	private String iso3;

	public Country() {
	}

	public Country(String name, String[] labels, String iso2, String iso3) {
		super(iso2, name, labels);
		this.iso2 = iso2;
		this.iso3 = iso3;
	}

	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}
}
