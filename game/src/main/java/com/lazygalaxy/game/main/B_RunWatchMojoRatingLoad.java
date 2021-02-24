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
import com.lazygalaxy.game.domain.Ratings;
import com.mongodb.client.model.Filters;

public class B_RunWatchMojoRatingLoad {

	private static final Logger LOGGER = LogManager.getLogger(B_RunWatchMojoRatingLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new WatchMojoRatingLoad().load("csv/watchmojo_mame_ratings.csv", 0, new FieldMerge<Ratings>());
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class WatchMojoRatingLoad extends CSVLoad<Ratings> {

		public WatchMojoRatingLoad() throws Exception {
			super(Ratings.class);
		}

		@Override
		protected Ratings getMongoDocument(String[] tokens) throws Exception {
			Integer rating = Integer.parseInt(tokens[0]);
			String name = GeneralUtil.alphanumerify(tokens[1]);
			Integer year = Integer.parseInt(tokens[2]);
			String system = GeneralUtil.alphanumerify(tokens[3]);

			List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(Filters.in("labels", name),
					Filters.eq("year", year), Filters.eq("system", system), Filters.ne("hide", true));
			if (games.size() == 0) {
				LOGGER.warn("game not found: " + name);
				return null;
			} else if (games.size() > 1) {
				LOGGER.warn("multiple games found: " + name);
				return null;
			}
			return new Ratings(games.get(0).id, null, rating);
		}
	}

}
