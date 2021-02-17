package com.lazygalaxy.game.main;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

public class A_RunRetroArchGameXMLLoad {
	private static final Logger LOGGER = LogManager.getLogger(A_RunRetroArchGameXMLLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new GameMerge();
			new GameLoad().load("xml/retroarch_atomiswave_games_vman_orig.xml", "game", merge, "System");
			new GameLoad().load("xml/retroarch_daphne_games_vman_orig.xml", "game", merge, "System");
			new GameLoad().load("xml/retroarch_mame_games_vman_orig.xml", "game", merge, "System");
			new GameLoad().load("xml/retroarch_naomi_games_vman_orig.xml", "game", merge, "System");
			new GameLoad().load("xml/retroarch_neogeo_games_vman_orig.xml", "game", merge, "System");
			LOGGER.info("xml load completed!");
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
			String name = XMLUtils.handleString(element, "name").replaceAll(" - ", ": ");
			String[] labels = new String[0];
			String system = GeneralUtil.alphanumerify(extraTagValues.get(0));
			String path = XMLUtils.handleString(element, "path");
			String description = XMLUtils.handleString(element, "desc");
			Double rating = null;
			Integer year = XMLUtils.handleInteger(element, "releasedate", 4);
			String developer = XMLUtils.handleString(element, "developer");
			String publisher = XMLUtils.handleString(element, "publisher");
			String genre = XMLUtils.handleString(element, "genre");
			Set<Integer> players = new TreeSet<Integer>();
			Boolean hide = XMLUtils.handleBoolean(element, "hide");

			String[] playerArray = XMLUtils.handleStringArray(element, "players", "-");
			if (playerArray != null) {
				if (playerArray.length == 1) {
					players.add(Integer.parseInt(playerArray[0]));
				} else if (playerArray.length == 2) {
					for (int i = Integer.parseInt(playerArray[0]); i <= Integer.parseInt(playerArray[1]); i++) {
						players.add(i);
					}
				} else {
					throw new Exception("unexpected players " + XMLUtils.handleString(element, "players"));
				}
			} else {
				players.add(1);
			}

			return new Game(system + path, name, labels, system, path, description, rating, year, developer, publisher,
					genre, players, hide);
		}
	}

	private static class GameMerge extends FieldMerge<Game> {
		public static final List<String> EXCLUDE_FIELDS = Arrays.asList("name", "updateDateTime", "labels", "players");

		@Override
		public void apply(Game newDocument, Game storedDocument) throws Exception {

			super.apply(newDocument, storedDocument);

			for (Field field : Game.class.getFields()) {
				if (!EXCLUDE_FIELDS.contains(field.getName())) {
					Object newValue = field.get(newDocument);
					Object storedValue = field.get(storedDocument);
					if (newValue != null && storedValue != null) {
						boolean merge = false;
						if (!newValue.equals(storedValue)) {
							if (StringUtils.equals(field.getName(), "genre")) {
								String[] newTokens = newDocument.genre.split("/");
								String[] storedTokens = newDocument.genre.split("/");
								if (storedTokens.length > newTokens.length) {
									newDocument.genre = storedDocument.genre;
									merge = true;
								}
							}
							if (!merge && (storedValue.toString().length() > newValue.toString().length())) {
								field.set(newDocument, storedValue);
							}
						}
					}
				}
			}
		}

	}
}
