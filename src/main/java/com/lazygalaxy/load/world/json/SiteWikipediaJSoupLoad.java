package com.lazygalaxy.load.world.json;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.world.Site;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.load.jsoup.JSoupTable;
import com.lazygalaxy.load.jsoup.TextJSoupElementProcessor;

public class SiteWikipediaJSoupLoad extends JSoupLoad<Site> {
	private static final Logger LOGGER = LogManager.getLogger(SiteWikipediaJSoupLoad.class);

	private TextJSoupElementProcessor descriptionProcessor = new TextJSoupElementProcessor(". ");
	private TitleJSoupElementProcessor titleProcessor = new TitleJSoupElementProcessor();

	public SiteWikipediaJSoupLoad() {
		super(Site.class);
	}

	@Override
	public void load(String title) throws Exception {
		super.load("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements tables = doc.select("table[class*=wikitable]");
		Set<String> linkSet = new LinkedHashSet<String>();
		for (Element table : tables) {
			JSoupTable jsoupTable = new JSoupTable(table);

			for (int i = 0; i < jsoupTable.getDataRows(); i++) {
				String description = jsoupTable.process(i, "site", descriptionProcessor)
						+ jsoupTable.process(i, "name", descriptionProcessor)
						+ jsoupTable.process(i, "description", descriptionProcessor);

				String[] title = jsoupTable.process(i, "site", titleProcessor);
				LOGGER.info(title + " " + description);
			}
		}

		return linkSet;
	}

	@Override
	public Site getMongoDocument(String html) throws Exception {
		LOGGER.info(html);
		return null;
	}

}
