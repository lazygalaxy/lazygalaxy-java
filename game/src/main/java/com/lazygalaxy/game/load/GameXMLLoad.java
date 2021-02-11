package com.lazygalaxy.game.load;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.load.XMLLoad;

public class GameXMLLoad extends XMLLoad<Game> {

	public GameXMLLoad() throws Exception {
		super(Game.class);
	}

	@Override
	protected Game getMongoDocument(Element element) throws Exception {
		String name = handleString(element, "name");
		String[] labels = new String[0];
		String system = "Mame";
		String path = handleString(element, "path");
		String description = handleString(element, "desc");
		Double rating = handleDouble(element, "rating");
		Integer year = handleInteger(element, "releasedate", 4);
		String developer = handleString(element, "developer");
		String publisher = handleString(element, "publisher");
		String genre = handleString(element, "genre");
		String players = handleString(element, "players");

		return new Game(name, labels, system, path, description, rating, year, developer, publisher, genre, players);
	}

	private String handleString(Element element, String tagName) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return nodes.item(0).getTextContent();
		}
		return null;
	}

	private Double handleDouble(Element element, String tagName) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return Double.parseDouble(nodes.item(0).getTextContent());
		}
		return null;
	}

	private Integer handleInteger(Element element, String tagName, int length) {
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes.getLength() > 0 && !StringUtils.isBlank(nodes.item(0).getTextContent())) {
			return Integer.parseInt(StringUtils.left(nodes.item(0).getTextContent(), length));
		}
		return null;
	}

}
