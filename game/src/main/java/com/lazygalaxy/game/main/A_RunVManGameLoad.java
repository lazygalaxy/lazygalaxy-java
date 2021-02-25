package com.lazygalaxy.game.main;

import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtils;
import com.lazygalaxy.game.domain.Game;

public class A_RunVManGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(A_RunVManGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new GameMerge();
			new GameLoad().load("xml/vman/retroarch_atomiswave_games.xml", "game", merge, "System");
			new GameLoad().load("xml/vman/retroarch_daphne_games.xml", "game", merge, "System");
			new GameLoad().load("xml/vman/retroarch_arcade_games.xml", "game", merge, "System");
			new GameLoad().load("xml/vman/retroarch_naomi_games.xml", "game", merge, "System");
			new GameLoad().load("xml/vman/retroarch_neogeo_games.xml", "game", merge, "System");
			LOGGER.info("games load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class GameLoad extends XMLLoad<Game> {

		public GameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected Game getMongoDocument(Element element, List<String> extraTagValues) throws Exception {

			String path = XMLUtils.handleString(element, "path");
			String romId = GeneralUtil
					.alphanumerify(StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, ".")));
			String systemId = GeneralUtil.alphanumerify(extraTagValues.get(0));

			String name = XMLUtils.handleString(element, "name");
			name = GeneralUtil.pretify(name);
			Game game = new Game(romId + "_" + systemId, name);

			game.addLabel(romId);

			String image = XMLUtils.handleString(element, "image");
			if (image != null) {
				String parentId = StringUtils.substring(image, 0, StringUtils.lastIndexOf(image, "."));
				parentId = StringUtils.substring(parentId, StringUtils.lastIndexOf(parentId, "/") + 1,
						parentId.length());

				if (!StringUtils.equals(parentId, romId)) {
					game.addLabel(parentId);
					game.parentId = parentId;
				}
			}

			game.romId = romId;
			game.systemId = systemId;
			game.year = XMLUtils.handleInteger(element, "releasedate", 4);
			game.path = path;
			game.developer = XMLUtils.handleString(element, "developer");
			game.publisher = XMLUtils.handleString(element, "publisher");
			game.description = GeneralUtil.pretify(XMLUtils.handleString(element, "desc"));

			// genre
			game.genre = new TreeSet<String>();
			String genreString = XMLUtils.handleString(element, "genre");
			if (genreString != null) {
				genreString = genreString.toLowerCase();
				genreString = genreString.replaceAll("plateform", "platform");
				genreString = genreString.replaceAll(" and ", " ");
				String[] genreArray = GeneralUtil.split(genreString, "[/,]");

				for (String token : genreArray) {
					String[] genreDetailArray = GeneralUtil.split(token, "-");
					if (genreDetailArray.length == 1) {
						game.genre.add(token);
					} else if (genreDetailArray.length == 2
							&& StringUtils.equals(genreDetailArray[0], genreDetailArray[1])) {
						game.genre.add(genreDetailArray[0]);
					} else {
						game.genre.add(token);
					}
				}
			}

			// players
			game.players = new TreeSet<Integer>();
			String[] playerArray = XMLUtils.handleStringArray(element, "players", "-");
			if (playerArray != null) {
				if (playerArray.length == 1) {
					game.players.add(Integer.parseInt(playerArray[0]));
				} else if (playerArray.length == 2) {
					for (int i = Integer.parseInt(playerArray[0]); i <= Integer.parseInt(playerArray[1]); i++) {
						game.players.add(i);
					}
				} else {
					throw new Exception("unexpected players " + XMLUtils.handleString(element, "players"));
				}
			} else {
				game.players.add(1);
			}

			game.hide = XMLUtils.handleBoolean(element, "hide");

			return game;
		}
	}

	private static class GameMerge extends FieldMerge<Game> {
		// public static final List<String> EXCLUDE_FIELDS = Arrays.asList("name",
		// "updateDateTime", "labels", "players",
		// "genre", "year");

		@Override
		public void apply(Game newDocument, Game storedDocument) throws Exception {

			super.apply(newDocument, storedDocument);

			newDocument.genre.addAll(storedDocument.genre);

//			if (!StringUtils.equals(newDocument.description, storedDocument.description)) {
//				LOGGER.info(newDocument.description);
//				LOGGER.info(storedDocument.description);
//			}

//			for (Field field : Game.class.getFields()) {
//				if (!EXCLUDE_FIELDS.contains(field.getName())) {
//					Object newValue = field.get(newDocument);
//					Object storedValue = field.get(storedDocument);
//					if (newValue != null && storedValue != null) {
//						if (!newValue.equals(storedValue)) {
//							if (storedValue.toString().length() > newValue.toString().length()) {
//								field.set(newDocument, storedValue);
//							}
//						}
//					}
//				}
//			}
		}

	}
}
