package com.lazygalaxy.game.main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;

public class SourceLoad {
	private static final Logger LOGGER = LogManager.getLogger(SourceLoad.class);

	protected static void sourceLoad(String source) throws Exception {
		GameMerge merge = new GameMerge();
		File scrapeDir = Paths.get(ClassLoader.getSystemResource("source/" + source).toURI()).toFile();
		for (File systemFile : scrapeDir.listFiles()) {
			new GameListLoad(source, systemFile.getName()).load(new File(systemFile, "gamelist.xml"), "game", merge);
			LOGGER.info(source + " " + systemFile.getName() + " enrich load completed!");
		}
	}

	private static class GameListLoad extends XMLLoad<Game> {
		private String source;
		private String systemId;

		public GameListLoad(String source, String systemId) throws Exception {
			super(Game.class);
			this.source = source;
			this.systemId = systemId;
		}

		@Override
		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String path = XMLUtil.getTagAsString(element, "path", 0);
			String romId = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
			romId = StringUtils.substring(romId, StringUtils.lastIndexOf(romId, "/") + 1, romId.length());

			Game game = new Game(romId, systemId);

			String name = XMLUtil.getTagAsString(element, "name", 0);
			String year = StringUtils.left(XMLUtil.getTagAsString(element, "releasedate", 0), 4);
			String description = XMLUtil.getTagAsString(element, "desc", 0);
			String genre = XMLUtil.getTagAsString(element, "genre", 0);
			String image = XMLUtil.getTagAsString(element, "image", 0);
			String video = XMLUtil.getTagAsString(element, "video", 0);
			String marquee = XMLUtil.getTagAsString(element, "marquee", 0);
			Double rating = XMLUtil.getTagAsDouble(element, "rating", 0);
			String originalPlayers = XMLUtil.getTagAsString(element, "players", 0);
			Integer players = null;
			if (!StringUtils.isBlank(originalPlayers)) {
				String[] playerArray = XMLUtil.getTagAsString(element, "players", 0).split("-");
				players = Integer.parseInt(playerArray[playerArray.length - 1]);
			}
			String developer = XMLUtil.getTagAsString(element, "developer", 0);
			String publisher = XMLUtil.getTagAsString(element, "publisher", 0);

			Game.class.getField(source + "GameInfo").set(game, new GameInfo(path, name, year, description, genre, image,
					video, marquee, rating, players, developer, publisher));

			return Arrays.asList(game);
		}
	}
}
