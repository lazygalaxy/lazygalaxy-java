package com.lazygalaxy.game.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Ratings;

public class C_RunGameRatingsLoad {
	private static final Logger LOGGER = LogManager.getLogger(C_RunGameRatingsLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();
			new GameRatingsLoad().load(merge);
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class GameRatingsLoad extends MongoLoad<Ratings, Game> {

		public GameRatingsLoad() throws Exception {
			super(Ratings.class, Game.class);
		}

		@Override
		protected Game getMongoDocument(Ratings ratings) throws Exception {
			Game game = new Game();
			game.id = ratings.id;

			if (ratings.screenScrapperFr != null) {
				game.rating = ratings.screenScrapperFr * 0.8;
			} else {
				game.rating = 0.0;
			}

			if (ratings.watchMojo != null) {
				game.rating += (1.0 - (ratings.watchMojo / 24.0)) * 0.2;
			}

			return game;
		}
	}

}
