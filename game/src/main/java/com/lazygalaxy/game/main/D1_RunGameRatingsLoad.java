package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;

public class D1_RunGameRatingsLoad {
	private static final Logger LOGGER = LogManager.getLogger(D1_RunGameRatingsLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();

			new GameRatingsLoad().load(merge);
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class GameRatingsLoad extends MongoLoad<Scores, Game> {

		public GameRatingsLoad() throws Exception {
			super(Scores.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Scores scores) throws Exception {
			Game game = new Game();
			game.id = scores.id;
			game.rating = 0.0;

			if (scores.vman != null) {
				game.rating += scores.vman;
			}

			if (scores.watchMojo != null) {
				game.rating += scores.watchMojo;
			}

			if (scores.antopisa != null) {
				game.rating += scores.antopisa;
			}

			if (scores.lazygalaxy != null) {
				game.rating += scores.lazygalaxy;
			}

			return Arrays.asList(game);
		}
	}

}
