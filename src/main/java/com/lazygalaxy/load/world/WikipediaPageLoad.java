package com.lazygalaxy.load.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.world.WikipediaPage;
import com.lazygalaxy.load.WikiMediaLoad;

import info.bliki.wiki.model.WikiModel;

public class WikipediaPageLoad extends WikiMediaLoad<WikipediaPage> {
	static final Logger LOGGER = LogManager.getLogger(WikipediaPageLoad.class);

	public WikipediaPageLoad() {
		super(WikipediaPage.class);
	}

	@Override
	protected List<WikipediaPage> getMongoDocuments(String content) throws Exception {
		List<WikipediaPage> sites = new ArrayList<WikipediaPage>();

		LOGGER.info(WikiModel.toHtml(content));

		return sites;
	}

}