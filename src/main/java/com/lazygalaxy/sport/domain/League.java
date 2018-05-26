package com.lazygalaxy.sport.domain;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class League extends MongoDocument {
	public String countryId;

	public League() {
	}

	public League(String name, String[] labels, Country country) {
		super(GeneralUtil.simplify(country.id) + "_" + GeneralUtil.simplify(name), name, labels);
		this.countryId = country.id;
	}
}
