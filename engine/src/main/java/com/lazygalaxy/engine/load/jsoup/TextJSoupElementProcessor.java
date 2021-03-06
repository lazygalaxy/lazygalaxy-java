package com.lazygalaxy.engine.load.jsoup;

import org.jsoup.nodes.Element;

public class TextJSoupElementProcessor implements JSoupElementProcessor<String> {
	private String append;

	public TextJSoupElementProcessor(String append) {
		this.append = append;
	}

	@Override
	public String apply(Element element) throws Exception {
		return element.text().trim() + append;
	}

	@Override
	public String empty() {
		return "";
	}

}
