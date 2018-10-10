package com.lazygalaxy.load.world.json;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.load.jsoup.JSoupElementProcessor;

public class TitleJSoupElementProcessor implements JSoupElementProcessor<String[]> {
	private static String[] EMPTY_ARRAY = new String[] {};

	@Override
	public String[] apply(Element element) {
		Elements links = element.select("a");

		List<String> list = new ArrayList<String>();
		for (Element link : links) {
			list.add(link.attr("title"));
		}

		return list.toArray(new String[list.size()]);
	}

	@Override
	public String[] empty() {
		return EMPTY_ARRAY;
	}

}
