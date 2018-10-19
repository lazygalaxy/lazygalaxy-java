package com.lazygalaxy.load.wikipedia;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.util.GeneralUtil;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

public class CoordsProcessor implements JSoupElementProcessor<Point> {
	@Override
	public Point apply(Element element) throws Exception {
		Elements latitudes = element.select("span[class*=latitude]");
		Elements longitudes = element.select("span[class*=longitude]");

		if (latitudes.size() == longitudes.size() && latitudes.size() > 0) {
			for (int i = 0; i < latitudes.size(); i++) {
				if (!StringUtils.equals(latitudes.get(0).text(), latitudes.get(i).text())
						|| !StringUtils.equals(longitudes.get(0).text(), longitudes.get(i).text())) {
					return empty();
				}
			}
			String[] latStr = GeneralUtil.numerify(latitudes.get(0).text(), ".", ",").split(",");
			String[] lonStr = GeneralUtil.numerify(longitudes.get(0).text(), ".", ",").split(",");

			double[] lat = new double[] { 0, 0, 0 };
			for (int i = 0; i < latStr.length; i++) {
				lat[i] = Double.parseDouble(latStr[i]);
			}
			double[] lon = new double[] { 0, 0, 0 };
			for (int i = 0; i < lonStr.length; i++) {
				lon[i] = Double.parseDouble(lonStr[i]);
			}

			return new Point(
					new Position(lat[0] + (lat[1] / 60) + (lat[2] / 3600), lon[0] + (lon[1] / 60) + (lon[2] / 3600)));
		}

		return empty();
	}

	@Override
	public Point empty() {
		return null;
	}
}
