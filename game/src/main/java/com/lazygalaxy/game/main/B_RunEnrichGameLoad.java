package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;
import com.mongodb.client.model.Filters;

public class B_RunEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B_RunEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new GameMerge();

			new CustomHideGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			LOGGER.info("hiding custom completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
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
}
