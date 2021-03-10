package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.mongodb.client.model.Filters;

public class B_RunEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B_RunEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();

			new DerivedEnrichGameLoad().load(merge);
			LOGGER.info("derived enrich completed!");

			new CustomEnrichGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			LOGGER.info("custom enrich completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class CustomEnrichGameLoad extends TextFileLoad<Game> {

		public CustomEnrichGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String rom) throws Exception {
			List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(Filters.eq("rom", rom));
			if (games.size() == 0) {
				LOGGER.info("No game found: " + rom);
			}
			if (games.size() > 1) {
				LOGGER.info("Multiple games found: " + rom);
			} else if (games.size() == 1) {
				for (Game game : games) {
					game.hide = true;
				}
				return games;
			}
			return null;
		}
	}

	private static class DerivedEnrichGameLoad extends MongoLoad<Game, Game> {

		public DerivedEnrichGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			switch (game.sourceFile) {
			case "naomi.cpp":
				game.systemId = GameSystem.NAOMI;
				break;
			case "neogeo.cpp":
				game.systemId = GameSystem.NEOGEO;
				break;
			case "playch10.cpp":
				game.systemId = GameSystem.PLAYCHOICE10;
				break;
			default:
				game.systemId = GameSystem.ARCADE;
			}

			if (StringUtils.equals(game.systemId, GameSystem.PLAYCHOICE10)) {
				game.hide = true;
			}
			return Arrays.asList(game);
		}
	}
}
