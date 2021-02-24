package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.mongodb.client.model.Filters;

public class C_RunWatchMojoScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C_RunWatchMojoScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new WatchMojoRatingLoad().load("csv/watchmojo_mame_score.csv", 0, new FieldMerge<Scores>());
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class WatchMojoRatingLoad extends CSVLoad<Scores> {

		public WatchMojoRatingLoad() throws Exception {
			super(Scores.class);
		}

		@Override
		protected Scores getMongoDocument(String[] tokens) throws Exception {
			Integer rating = Integer.parseInt(tokens[0]);
			String name = GeneralUtil.alphanumerify(tokens[1]);
			Integer year = Integer.parseInt(tokens[2]);
			String systemId = GeneralUtil.alphanumerify(tokens[3]);

			List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(Filters.in("labels", name),
					Filters.eq("year", year), Filters.eq("systemId", systemId), Filters.ne("hide", true));
			if (games.size() == 0) {
				LOGGER.warn("game not found: " + name + " " + year + " " + systemId);
				return null;
			} else if (games.size() > 1) {
				LOGGER.warn("multiple games found: " + name + " " + year + " " + systemId);
				return null;
			}
			Scores scores = new Scores(games.get(0).id);
			// (max*2) - 1
			scores.watchMojo = (int) Math.round(((47 - rating) / 46.0) * 100.0);
			return scores;
		}
	}

}
