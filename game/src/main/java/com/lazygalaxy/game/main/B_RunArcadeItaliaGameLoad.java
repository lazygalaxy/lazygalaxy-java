package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.game.domain.Game;
import com.mongodb.client.model.Filters;

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
		protected List<Game> getMongoDocument(String[] tokens) throws Exception {
			String gameId = tokens[0];

			List<Game> games = MongoHelper.getHelper(Game.class)
					.getDocumentsByFilters(Filters.or(Filters.in("romId", gameId), Filters.in("parentId", gameId)));

			if (games.size() == 0) {
				// LOGGER.warn("game not found: " + gameId);
				return null;
			} else if (games.size() > 1) {
				int unhiddenCounter = 0;
				for (Game game : games) {
					if (game.hide == null || !game.hide) {
						unhiddenCounter += 1;
					}
				}

				int arcadeNeoGeo = 0;
				if (unhiddenCounter == 2) {
					for (Game game : games) {
						if ((game.hide == null || !game.hide) && (StringUtils.equals(game.systemId, "arcade")
								|| StringUtils.equals(game.systemId, "neogeo"))) {
							arcadeNeoGeo += 1;
						}
					}
				}

				if (unhiddenCounter == 1 || (unhiddenCounter == 2 && arcadeNeoGeo == 2)) {
					// all good
				} else {
					LOGGER.warn("multiple games found: " + gameId);
				}
			}

			for (Game game : games) {
				game.isVeritcal = StringUtils.equals("VERTICAL", tokens[18]);
			}

			return games;
		}
	}
}
