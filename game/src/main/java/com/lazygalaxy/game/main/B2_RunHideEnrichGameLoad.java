package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

public class B2_RunHideEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B2_RunHideEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new DerivedHideEnrichGameLoad().load(merge);
			LOGGER.info("derived enrich completed!");

			new CustomHideEnrichGameLoad().load("hide/custom_hide.txt", 0, merge);
			LOGGER.info("custom hide enrich completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class DerivedHideEnrichGameLoad extends MongoLoad<Game, Game> {

		public DerivedHideEnrichGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			if (game.cloneOfRomId != null && !game.parentMissing) {
				game.hide = true;
			} else {
				game.hide = false;
			}
			return Arrays.asList(game);
		}
	}

	private static class CustomHideEnrichGameLoad extends TextFileLoad<Game> {

		public CustomHideEnrichGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String gameId) throws Exception {
			Game game = MongoHelper.getHelper(Game.class).getDocumentById(gameId);
			if (game != null) {
				game.hide = true;
				return Arrays.asList(game);
			} else {
				LOGGER.warn("game id not found:" + gameId);
			}
			return null;

		}
	}
}
