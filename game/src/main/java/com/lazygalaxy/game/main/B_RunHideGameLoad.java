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

public class B_RunHideGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B_RunHideGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();

			new CustomHideGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			LOGGER.info("hiding custom completed!");

			new SystemHideGameLoad().load(merge);
			LOGGER.info("hiding system completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class CustomHideGameLoad extends TextFileLoad<Game> {

		public CustomHideGameLoad() throws Exception {
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

	private static class SystemHideGameLoad extends MongoLoad<Game, Game> {

		public SystemHideGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			if (StringUtils.equals(game.systemId, GameSystem.PLAYCHOICE10)) {
				game.hide = true;
				return Arrays.asList(game);
			}
			return null;
		}
	}
}
