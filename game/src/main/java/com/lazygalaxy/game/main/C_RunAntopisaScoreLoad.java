package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.mongodb.client.model.Filters;

public class C_RunAntopisaScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C_RunAntopisaScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new AntopisaScoreLoad().load("txt/antopisa_score.ini", 8, new FieldMerge<Scores>());
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class AntopisaScoreLoad extends TextFileLoad<Scores> {
		private Integer lastScore = null;

		public AntopisaScoreLoad() throws Exception {
			super(Scores.class);
			lastScore = null;
		}

		@Override
		protected List<Scores> getMongoDocument(String line) throws Exception {
			String[] tokens = GeneralUtil.split(line, " ");
			if (tokens.length >= 4) {
				lastScore = Integer.parseInt(tokens[2]);
			}
			if (lastScore != null) {
				String gameId = GeneralUtil.alphanumerify(line);
				List<Game> games = MongoHelper.getHelper(Game.class)
						.getDocumentsByFilters(Filters.or(Filters.in("romId", gameId), Filters.in("parentId", gameId)));
				if (games.size() == 0) {
					if (lastScore.compareTo(100) >= 0) {
						LOGGER.warn("game not found: " + gameId);
					}
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
					} else if (lastScore.compareTo(100) >= 0) {
						LOGGER.warn("multiple games found: " + gameId);
					}
				}

				List<Scores> scoresList = new ArrayList<Scores>();

				for (Game game : games) {
					Scores scores = new Scores(game.id);
					scores.antopisa = lastScore;
					scoresList.add(scores);
				}

				return scoresList;
			}

			return null;
		}
	}

}
