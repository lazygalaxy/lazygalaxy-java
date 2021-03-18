package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.game.Collection;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

public class D2_RunCollectionsLoad {
	private static final Logger LOGGER = LogManager.getLogger(B1_RunHideEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new CollectionsLoad().load(merge);
			LOGGER.info("collections load completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class CollectionsLoad extends MongoLoad<Game, Game> {

		public CollectionsLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			game.collections = Collection.get(game);
			return Arrays.asList(game);
		}
	}
}
