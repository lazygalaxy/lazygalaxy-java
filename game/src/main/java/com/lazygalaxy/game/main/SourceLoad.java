package com.lazygalaxy.game.main;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;

public class SourceLoad {
	private static final Logger LOGGER = LogManager.getLogger(SourceLoad.class);

	protected static void sourceLoad(String source) throws Exception {
		GameMerge merge = new GameMerge();
		File scrapeDir = Paths.get(ClassLoader.getSystemResource("source/" + source).toURI()).toFile();
		for (File systemFile : scrapeDir.listFiles()) {
			if (systemFile.isDirectory()) {
				File systemGameListFile = new File(systemFile, "gamelist.xml");
				if (systemGameListFile.exists()) {
					new GameListLoad(source, systemFile.getName()).load(systemGameListFile, "game", merge);
					LOGGER.debug(source + " " + systemFile.getName() + " enrich load completed!");
				} else {
					LOGGER.warn(source + " " + systemFile.getName() + " no gamelist.xml found!");
				}
			}
		}
		LOGGER.warn("Finished loading: " + source);
	}

	private static class GameListLoad extends XMLLoad<Game> {
		private String source;
		private String systemId;
		private String defaultEmulator = null;
		private Map<String, String> emulatorMap = new HashMap<String, String>();

		public GameListLoad(String source, String systemId) throws Exception {
			super(Game.class);
			this.source = source;

			switch (systemId) {
			case Constant.GameEmulator.FBNEO:
				this.defaultEmulator = "lr-fbneo";
				this.systemId = Constant.GameSystem.ARCADE;
				break;
			case Constant.GameEmulator.MAME2003:
				this.defaultEmulator = "lr-mame2003";
				this.systemId = Constant.GameSystem.ARCADE;
				break;
			case Constant.GameEmulator.MAME2010:
				this.defaultEmulator = "lr-mame2010";
				this.systemId = Constant.GameSystem.ARCADE;
				break;
			default:
				URL fileURL = ClassLoader.getSystemResource("source/" + source + "/" + systemId + "/emulators.cfg");
				if (fileURL != null) {
					Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
					lines.forEach(line -> {
						if (line.startsWith("default")) {
							defaultEmulator = RegExUtils.removeAll(StringUtils.split(line, "=")[1], "\"").trim();
						}
					});
					lines.close();
				}
				fileURL = ClassLoader.getSystemResource("source/" + source + "/all/emulators.cfg");
				if (fileURL != null) {
					Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
					lines.forEach(line -> {
						String[] lineTokens = StringUtils.split(line, "=");
						String systemIdTemp = StringUtils.substring(lineTokens[0], 0,
								StringUtils.indexOf(lineTokens[0], "_"));
						String gameIdTemp = StringUtils.substring(lineTokens[0],
								StringUtils.indexOf(lineTokens[0], "_") + 1, lineTokens[0].length());

						emulatorMap.put(
								GeneralUtil.alphanumerify(systemIdTemp) + "_" + GeneralUtil.alphanumerify(gameIdTemp),
								RegExUtils.removeAll(lineTokens[1], "\"").trim());
					});
					lines.close();
				}

				this.systemId = systemId;
				break;
			}

		}

		@Override
		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String path = XMLUtil.getTagAsString(element, "path", 0);
			if (StringUtils.endsWith(path, ".sh")) {
				return null;
			}
			if (!GameSystem.MAME.contains(systemId)) {
				return null;
			}
			String gameId = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
			gameId = StringUtils.substring(gameId, StringUtils.indexOf(gameId, "/") + 1, gameId.length());

			Game game = new Game(GeneralUtil.alphanumerify(systemId), GeneralUtil.alphanumerify(gameId));

			String originalName = XMLUtil.getTagAsString(element, "name", 0);
			String year = XMLUtil.getTagAsString(element, "releasedate", 0);
			String description = XMLUtil.getTagAsString(element, "desc", 0);
			String genre = XMLUtil.getTagAsString(element, "genre", 0);
			String image = XMLUtil.getTagAsString(element, "image", 0);
			String video = XMLUtil.getTagAsString(element, "video", 0);
			String marquee = XMLUtil.getTagAsString(element, "marquee", 0);
			Double rating = XMLUtil.getTagAsDouble(element, "rating", 0);
			String players = XMLUtil.getTagAsString(element, "players", 0);
			String developer = XMLUtil.getTagAsString(element, "developer", 0);
			String publisher = XMLUtil.getTagAsString(element, "publisher", 0);

			Game.class.getField(source + "GameInfo").set(game,
					new GameInfo(game.gameId, path, originalName, year, description, genre, image, video, marquee,
							rating, players, developer, publisher,
							emulatorMap.containsKey(game.id) ? emulatorMap.get(game.id) : defaultEmulator));

			GameUtil.pretifyName((GameInfo) Game.class.getField(source + "GameInfo").get(game));

			game.addLabel(gameId);

			GameInfo gameInfo = (GameInfo) Game.class.getField(source + "GameInfo").get(game);

			if (gameInfo.uniqueNames != null) {
				for (String name : gameInfo.uniqueNames) {
					game.addLabel(name);
				}
			}

			return Arrays.asList(game);
		}
	}
}
