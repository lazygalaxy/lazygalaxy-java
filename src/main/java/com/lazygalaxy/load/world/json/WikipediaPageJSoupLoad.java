package com.lazygalaxy.load.world.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.world.WikipediaPage;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.load.jsoup.JSoupTable;
import com.lazygalaxy.load.jsoup.TextJSoupElementProcessor;

public class WikipediaPageJSoupLoad extends JSoupLoad<WikipediaPage> {
	private static final Logger LOGGER = LogManager.getLogger(WikipediaPageJSoupLoad.class);

	private TextJSoupElementProcessor descriptionProcessor = new TextJSoupElementProcessor(". ");
	private TitleJSoupElementProcessor titleProcessor = new TitleJSoupElementProcessor();

	public WikipediaPageJSoupLoad() {
		super(WikipediaPage.class);
	}

	@Override
	public void upsert(String title) throws Exception {
		super.upsert("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public void saveHTML(String title) throws Exception {
		super.saveHTML("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public void print(String title) throws Exception {
		super.print("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public List<WikipediaPage> getMongoDocuments(Document htmlDocument) throws Exception {
		Elements tables = htmlDocument.select("table[class*=wikitable]");
		List<WikipediaPage> pageList = new ArrayList<WikipediaPage>();
		for (Element table : tables) {
			JSoupTable jsoupTable = new JSoupTable(table);

			for (int i = 0; i < jsoupTable.getDataRows(); i++) {
				String summary = jsoupTable.process(i, "site", descriptionProcessor)
						+ jsoupTable.process(i, "name", descriptionProcessor)
						+ jsoupTable.process(i, "description", descriptionProcessor);

				List<String> titles = jsoupTable.process(i, "site", titleProcessor);
				titles.addAll(jsoupTable.process(i, "name", titleProcessor));
				for (String title : titles) {
					WikipediaPage page = new WikipediaPage(title, new String[] {});
					page.summary = summary;

					pageList.add(page);
				}
			}
		}

		return pageList;
	}

}
