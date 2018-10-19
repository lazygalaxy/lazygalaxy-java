package com.lazygalaxy.load.wikipedia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.WikipediaPage;
import com.lazygalaxy.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.load.wikipedia.InfoBoxProcessor.InfoBox;

public class WikipediaPageJSoupLoad extends JSoupLoad<WikipediaPage> {
	private static final Logger LOGGER = LogManager.getLogger(WikipediaPageJSoupLoad.class);

	private static final ImageProcessor IMAGE_PROCESSOR = new ImageProcessor();
	private static final InfoBoxProcessor INFO_BOX_PROCESSOR = new InfoBoxProcessor();
	private static final CoordsProcessor COORDS_PROCESSOR = new CoordsProcessor();
	private static final SummaryProcessor SUMMARY_PROCESSOR = new SummaryProcessor();

	private JSoupElementProcessor<List<WikipediaPage>> processor;

	public WikipediaPageJSoupLoad() {
		this(null);
	}

	public WikipediaPageJSoupLoad(JSoupElementProcessor<List<WikipediaPage>> processor) {
		super(WikipediaPage.class);

		this.processor = processor;
	}

	public void setProcessor(JSoupElementProcessor<List<WikipediaPage>> processor) {
		this.processor = processor;
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
		WikipediaPage page = new WikipediaPage(mainTitle, new String[] {});
		page.mustUpdate = true;
		page.summary = SUMMARY_PROCESSOR.apply(htmlDocument);
		page.image = IMAGE_PROCESSOR.apply(htmlDocument).get(0);
		page.coords = COORDS_PROCESSOR.apply(htmlDocument);
		page.updateDateTime = LocalDateTime.now();

		Elements tableElement = htmlDocument.select("table[class*=infobox]");
		if (tableElement.size() > 0) {
			InfoBox infoBox = INFO_BOX_PROCESSOR.apply(tableElement.get(0));
			// TODO tag as world heritage site
		}

		pageList.add(page);
		if (processor != null) {
			page.processor = processor.getClass().getName();
			pageList.addAll(processor.apply(htmlDocument));
		}

		return pageList;
	}

}
