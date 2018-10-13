package com.lazygalaxy.load.world.json;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.world.Image;
import com.lazygalaxy.domain.world.WikipediaPage;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.load.jsoup.JSoupTable;
import com.lazygalaxy.load.jsoup.TextJSoupElementProcessor;

public class WikipediaPageJSoupLoad extends JSoupLoad<WikipediaPage> {
	private static final Logger LOGGER = LogManager.getLogger(WikipediaPageJSoupLoad.class);

	private TextJSoupElementProcessor textProcessor = new TextJSoupElementProcessor(". ");
	private WikipediaTitleProcessor titleProcessor = new WikipediaTitleProcessor();
	private WikipediaImageProcessor imageProcessor = new WikipediaImageProcessor();

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
		List<WikipediaPage> pageList = new ArrayList<WikipediaPage>();

		String mainTitle = htmlDocument.select("h1").get(0).text();
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		WikipediaPage mainPage = helper.getDocumentByLabel(mainTitle);

		if (mainPage == null || mainPage.updateTime == null
				|| mainPage.updateTime.plusMinutes(1).isBefore(localDateTimeNow)) {

			WikipediaPage page = new WikipediaPage(mainTitle, new String[] {});
			page.updateTime = localDateTimeNow;
			page.summary = htmlDocument.select("p").get(0).text();
			page.image = imageProcessor.apply(htmlDocument).get(0);
			pageList.add(page);

			Elements tables = htmlDocument.select("table[class*=wikitable]");
			for (Element table : tables) {
				JSoupTable jsoupTable = new JSoupTable(table);

				for (int i = 0; i < jsoupTable.getDataRows(); i++) {
					List<String> titles = jsoupTable.process(i, "site", titleProcessor);
					titles.addAll(jsoupTable.process(i, "name", titleProcessor));
					String summary = jsoupTable.process(i, "site", textProcessor)
							+ jsoupTable.process(i, "name", textProcessor)
							+ jsoupTable.process(i, "description", textProcessor);
					List<Image> images = jsoupTable.process(i, "image", imageProcessor);

					for (String title : titles) {
						WikipediaPage currentPage = helper.getDocumentByLabel(title);

						if (currentPage == null || currentPage.updateTime == null) {
							page = new WikipediaPage(title, new String[] {});
							page.summary = summary;
							if (!images.isEmpty()) {
								page.image = images.get(0);
							}
							pageList.add(page);
						}
					}
				}
			}
		} else {
			LOGGER.info("skipping as page was updated recently: " + mainPage.id);
		}

		return pageList;
	}

}
