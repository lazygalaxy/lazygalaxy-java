package com.lazygalaxy.game.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XX_RunGameRatingsLoad {
	private static final Logger LOGGER = LogManager.getLogger(XX_RunGameRatingsLoad.class);

//	public static void main(String[] args) throws Exception {
//		try {
//			Merge<Game> merge = new FieldMerge<Game>();
//
//			new GameRatingsLoad().load(merge);
//			LOGGER.info("xml load completed!");
//		} finally {
//			MongoConnectionHelper.INSTANCE.close();
//		}
//	}
//
//	private static class GameRatingsLoad extends MongoLoad<Scores, Game> {
//
//		public GameRatingsLoad() throws Exception {
//			super(Scores.class, Game.class);
//		}
//
//		@Override
//		protected List<Game> getMongoDocument(Scores scores) throws Exception {
//			Game game = new Game();
//			game.id = scores.id;
//			game.rating = 0.0;
//
//			if (scores.vman != null) {
//				game.rating += scores.vman;
//			}
//
//			if (scores.watchMojo != null) {
//				game.rating += scores.watchMojo;
//			}
//
//			if (scores.antopisa != null) {
//				game.rating += scores.antopisa;
//			}
//
//			if (scores.lazygalaxy != null) {
//				game.rating += scores.lazygalaxy;
//			}
//
//			if (scores.rickDangerous != null) {
//				game.rating += scores.rickDangerous;
//			}
//
//			return Arrays.asList(game);
//		}
//	}

}
