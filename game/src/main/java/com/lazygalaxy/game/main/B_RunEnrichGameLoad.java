package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class B_RunEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B_RunEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new GameMerge();

			new HideDuplicateGameLoad().load(merge,
					Filters.or(Filters.in("systemId", "neogeo"), Filters.in("systemId", "naomi")));
			LOGGER.info("hiding duplicates completed!");

			new CustomHideGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			LOGGER.info("hiding custom completed!");

			new ArcadeItaliaEnrichGameLoad().load("csv/arcadeitalia_games.csv", 1, merge);
			LOGGER.info("arcade italia enrichment completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class HideDuplicateGameLoad extends MongoLoad<Game, Game> {

		public HideDuplicateGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game storedDocument) throws Exception {
			List<Game> games = MongoHelper.getHelper(Game.class)
					.getDocumentsByFilters(Filters.in("romId", storedDocument.romId));
			if (games.size() == 0) {
				LOGGER.info("No game found: " + storedDocument.romId);
			} else if (games.size() > 1) {
				for (Game game : games) {
					if (StringUtils.equals(game.systemId, "arcade")
							|| StringUtils.equals(game.systemId, "atomiswave")) {
						game.hide = true;
					}
				}
				return games;
			}
			return null;
		}
	}

	private static class CustomHideGameLoad extends TextFileLoad<Game> {

		public CustomHideGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String romId) throws Exception {
			List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(Filters.in("romId", romId));
			if (games.size() == 0) {
				LOGGER.info("No game found: " + romId);
			}
			if (games.size() > 1) {
				LOGGER.info("Multiple games found: " + romId);
			} else if (games.size() == 1) {
				for (Game game : games) {
					game.hide = true;
				}
				return games;
			}
			return null;
		}

	}

	private static class ArcadeItaliaEnrichGameLoad extends CSVLoad<Game> {

		public ArcadeItaliaEnrichGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String[] tokens) throws Exception {
			String gameId = tokens[0];

			List<Game> games = GameUtil.getGames(false, true, gameId,
					Filters.or(Filters.in("romId", gameId), Filters.in("alternativeId", gameId)));

			if (games != null) {
				for (Game game : games) {
					game.isVeritcal = StringUtils.equals("VERTICAL", tokens[18]);
				}

				return games;
			}
			return null;
		}
	}

}
