package com.lazygalaxy.sport.domain;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class Team extends MongoDocument {
	public String countryId;
	public Integer whoScoredId;

	public Team() {
	}

	public Team(String name, String[] labels, Country country, Integer whoScoredId) {
		super(GeneralUtil.alphanumerify(country.id) + "_" + GeneralUtil.alphanumerify(name), name, labels);
		this.countryId = country.id;
		this.whoScoredId = whoScoredId;
	}
}
