package com.lazygalaxy.game.main;

import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.Genre;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;

public class A_RunVManGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(A_RunVManGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new GameMerge();
			new RetroArchGameLoad().load("xml/vman/retroarch_arcade_games.xml", "game", merge, "System", "collection");
			new RetroArchGameLoad().load("xml/vman/retroarch_atomiswave_games.xml", "game", merge, "System",
					"collection");
			new RetroArchGameLoad().load("xml/vman/retroarch_daphne_games.xml", "game", merge, "System", "collection");
			new RetroArchGameLoad().load("xml/vman/retroarch_naomi_games.xml", "game", merge, "System", "collection");
			new RetroArchGameLoad().load("xml/vman/retroarch_neogeo_games.xml", "game", merge, "System", "collection");
			LOGGER.info("vman games load completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class RetroArchGameLoad extends XMLLoad<Game> {

		public RetroArchGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected Game getMongoDocument(Element element, List<String> extraTagValues) throws Exception {

			String path = XMLUtil.getTagAsString(element, "path");
			String romId = GeneralUtil
					.alphanumerify(StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, ".")));
			String systemId = GeneralUtil.alphanumerify(extraTagValues.get(0));

			String name = XMLUtil.getTagAsString(element, "name");
			name = GameUtil.pretify(name);
			Game game = new Game(romId + "_" + systemId, name);

			game.addLabel(romId);

			String image = XMLUtil.getTagAsString(element, "image");
			if (image != null) {
				String alternativeId = StringUtils.substring(image, 0, StringUtils.lastIndexOf(image, "."));
				alternativeId = StringUtils.substring(alternativeId, StringUtils.lastIndexOf(alternativeId, "/") + 1,
						alternativeId.length());

				if (!StringUtils.equals(alternativeId, romId)) {
					game.addLabel(alternativeId);
					game.alternativeId = alternativeId;
				}
			}

			game.romId = romId;
			game.systemId = systemId;
			game.year = XMLUtil.getTagAsInteger(element, "releasedate", 4);
			game.manufacturers = new TreeSet<String>();

			String manufacturer = XMLUtil.getTagAsString(element, "developer");
			if (StringUtils.startsWith(manufacturer, "Data East")) {
				game.manufacturers.add("Data East");
			}
			manufacturer = XMLUtil.getTagAsString(element, "publisher");
			if (StringUtils.startsWith(manufacturer, "Data East")) {
				game.manufacturers.add("Data East");
			}
			game.description = GameUtil.pretify(XMLUtil.getTagAsString(element, "desc"));

			// genre
			game.genre = new TreeSet<String>();
			String genreString = XMLUtil.getTagAsString(element, "genre");
			if (genreString != null) {
				genreString = genreString.toLowerCase();
				genreString = genreString.replaceAll("plateform", "platform");
				genreString = genreString.replaceAll(" and ", " ");
				String[] genreArray = GeneralUtil.split(genreString, "[/,-]");

				for (String token : genreArray) {
					token = token.replace("swimming", Genre.POOL);
					token = token.replace("sports", Genre.SPORT);
					token = token.replace("lightgun shooter", Genre.LIGHTGUN);
					game.addGenre(token);
				}
			}

			// players
			game.players = new TreeSet<Integer>();
			String[] playerArray = XMLUtil.getTagAsStringArray(element, "players", "-");
			if (playerArray != null) {
				if (playerArray.length == 1) {
					game.players.add(Integer.parseInt(playerArray[0]));
				} else if (playerArray.length == 2) {
					for (int i = Integer.parseInt(playerArray[0]); i <= Integer.parseInt(playerArray[1]); i++) {
						game.players.add(i);
					}
				} else {
					throw new Exception("unexpected players " + XMLUtil.getTagAsString(element, "players"));
				}
			} else {
				game.players.add(1);
			}

			String collection = GeneralUtil.alphanumerify(extraTagValues.get(1));
			game.collections = new TreeSet<String>();
			game.collections.add(collection);

			game.hide = XMLUtil.getTagAsBoolean(element, "hide");

			return game;
		}
	}
}
