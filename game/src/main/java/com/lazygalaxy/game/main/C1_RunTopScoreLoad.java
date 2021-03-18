package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C1_RunTopScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C1_RunTopScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Scores> merge = new FieldMerge<Scores>();

			new WatchMojoScoreLoad().load("csv/watchmojo_score.csv", 0, merge);
			LOGGER.info("watch mojo load completed!");

			new LazyGalaxyScoreLoad().load("txt/lazygalaxy_favourite.txt", 0, merge);
			LOGGER.info("lazygalaxy load completed!");
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
			String year = tokens[2];

			List<Game> games = GameUtil.getGames(true, true, name + " " + year, Filters.in("labels", name),
					Filters.eq("year", year));

			if (games != null) {
				List<Scores> scoresList = new ArrayList<Scores>();

				for (Game game : games) {
					Scores scores = new Scores(game.id);
					// (max*2) - 1
					scores.watchMojo = (int) Math.round(((46 - rating) / 45.0) * 100.0);
					scoresList.add(scores);
				}

				return scoresList;
			}
			return null;
		}
	}

	private static class LazyGalaxyScoreLoad extends TextFileLoad<Scores> {

		public LazyGalaxyScoreLoad() throws Exception {
			super(Scores.class);
		}

		@Override
		protected List<Scores> getMongoDocument(String rom) throws Exception {
			List<Game> games = GameUtil.getGames(true, true, rom, Filters.eq("rom", rom));

			if (games != null) {
				List<Scores> scoresList = new ArrayList<Scores>();

				for (Game game : games) {
					Scores scores = new Scores(game.id);

					scores.lazygalaxy = 100;
					scoresList.add(scores);
				}

				return scoresList;
			}
			return null;
		}
	}

}
