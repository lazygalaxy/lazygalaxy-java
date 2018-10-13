package com.lazygalaxy.load.jsoup;

import org.jsoup.nodes.Element;

public interface JSoupElementProcessor<T> {
	T apply(Element element) throws Exception;

	T empty();
}
