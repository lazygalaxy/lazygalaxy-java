package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C_RunWatchMojoScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C_RunWatchMojoScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new WatchMojoScoreLoad().load("csv/watchmojo_score.csv", 0, new FieldMerge<Scores>());
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class WatchMojoScoreLoad extends CSVLoad<Scores> {

		public WatchMojoScoreLoad() throws Exception {
			super(Scores.class);
		}

		@Override
		protected List<Scores> getMongoDocument(String[] tokens) throws Exception {
			Integer rating = Integer.parseInt(tokens[0]);
			String name = GeneralUtil.alphanumerify(tokens[1]);
			Integer year = Integer.parseInt(tokens[2]);

			List<Game> games = GameUtil.getGames(true, true, name + " " + year, Filters.in("labels", name),
					Filters.eq("year", year));

			if (games != null) {
				List<Scores> scoresList = new ArrayList<Scores>();

				for (Game game : games) {
					Scores scores = new Scores(game.id);
					// (max*2) - 1
					scores.watchMojo = (int) Math.round(((47 - rating) / 46.0) * 100.0);
					scoresList.add(scores);
				}

				return scoresList;
			}
			return null;
		}
	}

}
