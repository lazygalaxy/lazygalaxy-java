package com.lazygalaxy.domain.world;

import com.lazygalaxy.domain.MongoDocument;

public class Site extends MongoDocument {
	public String siteId;

	public Site() {
	}

	public Site(String name, String[] labels, Site site) throws Exception {
		super(buildId("_", name, site.id), name, labels);
		this.siteId = site.id;
	}
}
