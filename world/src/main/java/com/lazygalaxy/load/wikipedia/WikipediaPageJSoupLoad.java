package com.lazygalaxy.load.wikipedia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.WikipediaPage;
import com.lazygalaxy.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.load.wikipedia.InfoBoxProcessor.InfoBox;
import com.lazygalaxy.util.GeneralUtil;

public class WikipediaPageJSoupLoad extends JSoupLoad<WikipediaPage> {
	private static final ImageProcessor IMAGE_PROCESSOR = new ImageProcessor();
	private static final InfoBoxProcessor INFO_BOX_PROCESSOR = new InfoBoxProcessor();
	private static final CoordsProcessor COORDS_PROCESSOR = new CoordsProcessor();
	private static final SummaryProcessor SUMMARY_PROCESSOR = new SummaryProcessor();

	private JSoupElementProcessor<List<WikipediaPage>> processor;
	private String title;

	public WikipediaPageJSoupLoad() throws Exception {
		this(null);
	}

	public WikipediaPageJSoupLoad(JSoupElementProcessor<List<WikipediaPage>> processor) throws Exception {
		super(WikipediaPage.class);

		this.processor = processor;
	}

	public void setProcessor(JSoupElementProcessor<List<WikipediaPage>> processor) {
		this.processor = processor;
	}

	@Override
	public void upsert(String title) throws Exception {
		this.title = title;
		super.upsert("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public void saveHTML(String title) throws Exception {
		this.title = title;
		super.saveHTML("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public void print(String title) throws Exception {
		this.title = title;
		super.print("https://en.wikipedia.org/wiki/" + title);
	}

	@Override
	public List<WikipediaPage> getMongoDocuments(Document htmlDocument) throws Exception {
		List<WikipediaPage> pageList = new ArrayList<WikipediaPage>();
		if (htmlDocument != null) {
			String mainTitle = htmlDocument.select("h1").get(0).text();
			WikipediaPage page = new WikipediaPage(mainTitle, new String[] {});
			if (title != null) {
				page.labels.add(GeneralUtil.alphanumerify(title));
			}
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
		}
		return pageList;
	}

}
