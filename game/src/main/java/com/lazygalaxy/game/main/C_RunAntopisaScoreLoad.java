package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C_RunAntopisaScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C_RunAntopisaScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Scores> merge = new FieldMerge<Scores>();

			new AntopisaScoreLoad().load("txt/antopisa_score.ini", 8, merge);
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
			} else if (lastScore != null) {
				String rom = GeneralUtil.alphanumerify(line);

				List<Game> games = GameUtil.getGames(true, true, rom,
						Filters.or(Filters.eq("rom", rom), Filters.in("clones", rom)));

				if (games != null) {
					List<Scores> scoresList = new ArrayList<Scores>();
					for (Game game : games) {
						Scores scores = new Scores(game.id);
						scores.antopisa = lastScore;
						scoresList.add(scores);
					}

					return scoresList;
				}
			}

			return null;
		}
	}

}
