package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

public class E3_RunLazyGalaxyEnrichLoad {
	private static final Logger LOGGER = LogManager.getLogger(E3_RunLazyGalaxyEnrichLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new LazyGalaxyHideLoad().load("lazygalaxy/lazygalaxy_hide.txt", 0, merge);
			LOGGER.info("lazygalaxy hide game completed!");

			new LazyGalaxyFavouriteLoad().load("lazygalaxy/lazygalaxy_favourite.txt", 0, merge);
			LOGGER.info("lazygalaxy favourite game completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class LazyGalaxyHideLoad extends TextFileLoad<Game> {

		public LazyGalaxyHideLoad() throws Exception {
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

	private static class LazyGalaxyFavouriteLoad extends TextFileLoad<Game> {

		public LazyGalaxyFavouriteLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String gameId) throws Exception {
			Game game = MongoHelper.getHelper(Game.class).getDocumentById(gameId);
			if (game != null) {
				game.favourite = true;
				return Arrays.asList(game);
			} else {
				LOGGER.warn("game id not found:" + gameId);
			}
			return null;

		}
	}
}
