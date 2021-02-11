package com.lazygalaxy.world.load.wikipedia;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.engine.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.engine.load.jsoup.TableJSoupElementProcessor;
import com.lazygalaxy.engine.load.jsoup.TableJSoupElementProcessor.JSoupTable;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.world.domain.WikipediaPage;

public class WHSParentProcessor implements JSoupElementProcessor<List<WikipediaPage>> {
	private static final TitleProcessor TITLE_PROCESSOR = new TitleProcessor();
	private static final TableJSoupElementProcessor TABLE_PROCESSOR = new TableJSoupElementProcessor();

	@Override
	public List<WikipediaPage> apply(Element element) throws Exception {
		List<WikipediaPage> pageList = new ArrayList<WikipediaPage>();

		Elements tableElements = element.select("table[class*=wikitable]");
		for (Element tableElement : tableElements) {
			JSoupTable jsoupTable = TABLE_PROCESSOR.apply(tableElement);

			for (int i = 0; i < jsoupTable.getDataRows(); i++) {
				List<String> titles = jsoupTable.process(i, "country", TITLE_PROCESSOR);

				for (String title : titles) {
					// we want to add the world heritage sites by country
					if (GeneralUtil.alphanumerify(title).contains("worldheritagesite")) {
						WikipediaPage page = new WikipediaPage(title, new String[] {});
						page.processor = WHSByCountryProcessor.class.getName();
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
