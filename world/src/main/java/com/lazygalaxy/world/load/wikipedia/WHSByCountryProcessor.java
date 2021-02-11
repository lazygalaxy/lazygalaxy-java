package com.lazygalaxy.world.load.wikipedia;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.engine.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.engine.load.jsoup.TableJSoupElementProcessor;
import com.lazygalaxy.engine.load.jsoup.TextJSoupElementProcessor;
import com.lazygalaxy.engine.load.jsoup.TableJSoupElementProcessor.JSoupTable;
import com.lazygalaxy.world.domain.Image;
import com.lazygalaxy.world.domain.WikipediaPage;
import com.mongodb.client.model.geojson.Point;

public class WHSByCountryProcessor implements JSoupElementProcessor<List<WikipediaPage>> {
	private static final TextJSoupElementProcessor TEXT_PROCESSOR = new TextJSoupElementProcessor(". ");
	private static final ImageProcessor IMAGE_PROCESSOR = new ImageProcessor();
	private static final TitleProcessor TITLE_PROCESSOR = new TitleProcessor();
	private static final TableJSoupElementProcessor TABLE_PROCESSOR = new TableJSoupElementProcessor();
	private static final CoordsProcessor COORDS_PROCESSOR = new CoordsProcessor();

	@Override
	public List<WikipediaPage> apply(Element element) throws Exception {
		List<WikipediaPage> pageList = new ArrayList<WikipediaPage>();

		Elements tables = element.select("table[class*=wikitable]");
		for (Element tableElement : tables) {
			JSoupTable jsoupTable = TABLE_PROCESSOR.apply(tableElement);

			for (int i = 0; i < jsoupTable.getDataRows(); i++) {
				List<String> titles = jsoupTable.process(i, "site", TITLE_PROCESSOR);
				titles.addAll(jsoupTable.process(i, "name", TITLE_PROCESSOR));

				String summary = jsoupTable.process(i, "site", TEXT_PROCESSOR)
						+ jsoupTable.process(i, "name", TEXT_PROCESSOR)
						+ jsoupTable.process(i, "description", TEXT_PROCESSOR);
				List<Image> images = jsoupTable.process(i, "image", IMAGE_PROCESSOR);
				Point coords = jsoupTable.process(i, "location", COORDS_PROCESSOR);

				for (String title : titles) {
					if (!StringUtils.isBlank(title)) {
						WikipediaPage page = new WikipediaPage(title, new String[] {});
						page.summary = summary;
						page.coords = coords;
						if (!images.isEmpty()) {
							page.image = images.get(0);
						}
						pageList.add(page);
					}
				}
			}
		}

		return pageList;
	}

	@Override
	public List<WikipediaPage> empty() {
		return new ArrayList<WikipediaPage>();
	}

}
