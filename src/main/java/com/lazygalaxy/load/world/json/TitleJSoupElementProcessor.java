package com.lazygalaxy.load.world.json;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.load.jsoup.JSoupElementProcessor;

public class TitleJSoupElementProcessor implements JSoupElementProcessor<List<String>> {
	@Override
	public List<String> apply(Element element) {
		Elements links = element.select("a");

		List<String> list = new ArrayList<String>();
		for (Element link : links) {
			list.add(link.attr("title").trim());
		}

		return list;
	}

	@Override
	public List<String> empty() {
		return new ArrayList<>();
	}

}
