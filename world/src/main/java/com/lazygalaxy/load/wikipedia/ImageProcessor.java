package com.lazygalaxy.load.wikipedia;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.Image;
import com.lazygalaxy.load.jsoup.JSoupElementProcessor;

public class ImageProcessor implements JSoupElementProcessor<List<Image>> {
	@Override
	public List<Image> apply(Element element) throws Exception {
		Elements images = element.select("img");

		List<Image> list = new ArrayList<Image>();
		for (Element imageElement : images) {
			String link = imageElement.attr("src");
			if (!StringUtils.isBlank(link)) {
				link = "http:" + link;
				link = link.replaceAll("/thumb", "");
				int endIndex = link.length() - StringUtils.reverse(link).indexOf("/") - 1;
				link = link.substring(0, endIndex);

				String id = link.substring(link.length() - StringUtils.reverse(link).indexOf("/"), link.length());
				String caption = imageElement.attr("alt");
				list.add(new Image(id, caption, link));
			}
		}

		return list;
	}

	@Override
	public List<Image> empty() {
		return new ArrayList<Image>();
	}

}
