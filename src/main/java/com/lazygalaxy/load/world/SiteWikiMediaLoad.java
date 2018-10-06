package com.lazygalaxy.load.world;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.world.Site;
import com.lazygalaxy.load.WikiMediaLoad;

public class SiteWikiMediaLoad extends WikiMediaLoad<Site> {
	static final Logger LOGGER = LogManager.getLogger(SiteWikiMediaLoad.class);

	public SiteWikiMediaLoad() {
		super(Site.class);
	}

	@Override
	protected List<Site> getMongoDocuments(String content) throws Exception {
		LOGGER.info(content);
		return null;
	}

}