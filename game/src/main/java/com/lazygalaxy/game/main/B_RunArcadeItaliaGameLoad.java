package com.lazygalaxy.game.main;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;

public class B_RunArcadeItaliaGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B_RunArcadeItaliaGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();
			new GameLoad().load("csv/arcadeitalia_games.csv", 1, merge);
			LOGGER.info("games load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class GameLoad extends CSVLoad<Game> {

		public GameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected Game getMongoDocument(String[] tokens) throws Exception {
			Game game = new Game(tokens[0], tokens[1]);
			if (!StringUtils.contains(tokens[2], "?")) {
				game.year = Integer.parseInt(tokens[2]);
			}
			game.developer = tokens[3];
			game.publisher = tokens[3];
			if (!StringUtils.equals(tokens[4], "-")) {
				game.systemId = tokens[4];
			} else {
				game.systemId = "arcade";
			}
			game.isVeritcal = StringUtils.equals("VERTICAL", tokens[18]);

			return game;
		}
	}
}
