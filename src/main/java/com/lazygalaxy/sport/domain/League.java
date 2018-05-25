package com.lazygalaxy.sport.domain;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class League extends MongoDocument {
	private String countryId;

	public League() {
	}

	public League(String name, String[] labels, Country country) {
		super(GeneralUtil.simplify(country.getId()) + "_" + GeneralUtil.simplify(name), name, labels);
		this.countryId = country.getId();
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
}
