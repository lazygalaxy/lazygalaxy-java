package com.lazygalaxy.load.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.world.Site;
import com.lazygalaxy.load.WikiMediaLoad;

import info.bliki.wiki.model.WikiModel;

public class SiteWikiMediaLoad extends WikiMediaLoad<Site> {
	static final Logger LOGGER = LogManager.getLogger(SiteWikiMediaLoad.class);

	public SiteWikiMediaLoad() {
		super(Site.class);
	}

	@Override
	protected List<Site> getMongoDocuments(String content) throws Exception {
		List<Site> sites = new ArrayList<Site>();

		LOGGER.info(WikiModel.toHtml(content));

		return sites;
	}

}