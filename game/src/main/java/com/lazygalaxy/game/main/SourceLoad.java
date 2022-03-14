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
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;

public class SourceLoad {
	private static final Logger LOGGER = LogManager.getLogger(SourceLoad.class);

	protected static void sourceLoad(String source) throws Exception {
		GameMerge merge = new GameMerge();
		File scrapeDir = Paths.get(ClassLoader.getSystemResource("source/" + source + "/roms").toURI()).toFile();
		for (File systemFile : scrapeDir.listFiles()) {
			if (systemFile.isDirectory()) {
				new GameListLoad(source, systemFile.getName()).load(new File(systemFile, "gamelist.xml"), "game",
						merge);
				LOGGER.info(source + " " + systemFile.getName() + " enrich load completed!");
			}
		}
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
			case Constant.GameEmulator.MAME2003:
			case Constant.GameEmulator.MAME2010:
				this.defaultEmulator = systemId;
				this.systemId = Constant.GameSystem.ARCADE;
				break;
			default:
				URL fileURL = ClassLoader
						.getSystemResource("source/" + source + "/roms/" + systemId + "/emulators.cfg");
				if (fileURL != null) {
					Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
					lines.forEach(line -> {
						if (line.startsWith("default")) {
							defaultEmulator = RegExUtils.removeAll(StringUtils.split(line, "=")[1], "\"").trim();
						}
					});
					lines.close();
				}
				fileURL = ClassLoader.getSystemResource("source/" + source + "/roms/emulators.cfg");
				if (fileURL != null) {
					Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
					lines.forEach(line -> {
						String[] tokens = StringUtils.split(line, "=");
						emulatorMap.put(tokens[0].trim(), RegExUtils.removeAll(tokens[1], "\"").trim());
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
			String romFile = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
			romFile = StringUtils.substring(romFile, StringUtils.lastIndexOf(romFile, "/") + 1, romFile.length());

			Game game = new Game(GeneralUtil.alphanumerify(systemId), GeneralUtil.alphanumerify(romFile));

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

			Game.class.getField(source + "GameInfo").set(game,
					new GameInfo(path, name, year, description, genre, image, video, marquee,
							rating != null && rating > 0 ? rating : null, players, developer, publisher,
							emulatorMap.containsKey(game.id) ? emulatorMap.get(game.id) : defaultEmulator));

			// defaults
			if (game.isVeritcal == null) {
				game.isVeritcal = false;
			}

			if (game.hide == null) {
				game.hide = false;
			}

			if (game.parentMissing == null) {
				game.parentMissing = false;
			}

			return Arrays.asList(game);
		}
	}
}
