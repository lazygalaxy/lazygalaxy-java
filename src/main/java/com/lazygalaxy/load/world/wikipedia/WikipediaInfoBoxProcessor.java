package com.lazygalaxy.load.world.wikipedia;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.load.world.wikipedia.WikipediaInfoBoxProcessor.InfoBox;
import com.lazygalaxy.utils.GeneralUtil;

public class WikipediaInfoBoxProcessor implements JSoupElementProcessor<InfoBox> {
	private static final Logger LOGGER = LogManager.getLogger(WikipediaInfoBoxProcessor.class);

	public class InfoBox {
		protected String title;
		protected Map<String, Element> infoMap = new HashMap<String, Element>();

		public String getTitle() {
			return title;
		}

		public Element getElement(String key) {
			return infoMap.get(GeneralUtil.alphanumerify(key));
		}
	}

	@Override
	public InfoBox apply(Element element) throws Exception {
		InfoBox infoBox = new InfoBox();

		Elements rows = element.select("tr");
		infoBox.title = rows.remove(0).select("th,td").text();

		for (Element row : rows) {
			Elements data = row.select("th,td");
			if (data.size() == 2) {
				infoBox.infoMap.put(GeneralUtil.alphanumerify(data.get(0).text()), data.get(1));
			}
		}

		return infoBox;
	}

	@Override
	public InfoBox empty() {
		return new InfoBox();
	}
}
