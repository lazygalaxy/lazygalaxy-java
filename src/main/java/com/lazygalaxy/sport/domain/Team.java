package com.lazygalaxy.sport.domain;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class Team extends MongoDocument {
	private String countryId;
	private Integer whoScoredId;

	public Team() {
	}

	public Team(String name, String[] labels, Country country, Integer whoScoredId) {
		super(GeneralUtil.simplify(country.getId()) + "_" + GeneralUtil.simplify(name), name, labels);
		this.countryId = country.getId();
		this.whoScoredId = whoScoredId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public Integer getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(Integer whoScoredId) {
		this.whoScoredId = whoScoredId;
	}
}
