package com.lazygalaxy.domain;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.util.GeneralUtil;

public class League extends MongoDocument {
	public String countryId;
	public Integer whoScoredId;

	public League() {
	}

	public League(String name, String[] labels, Country country, Integer whoScoredId) throws Exception {
		super(buildId("", country.id, GeneralUtil.alphanumerify(name)), name, labels);
		this.countryId = country.id;
		this.whoScoredId = whoScoredId;
	}
}
