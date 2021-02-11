package com.lazygalaxy.world.load.wikipedia;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.engine.load.jsoup.JSoupElementProcessor;

public class TitleProcessor implements JSoupElementProcessor<List<String>> {
	@Override
	public List<String> apply(Element element) throws Exception {
		Elements links = element.select("a");

		List<String> list = new ArrayList<String>();
		for (Element link : links) {
			list.add(link.attr("title").trim());
		}

		return list;
	}

	@Override
	public List<String> empty() {
		return new ArrayList<String>();
	}

}
