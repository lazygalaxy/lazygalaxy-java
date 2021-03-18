package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class XX_CheckGameDuplicates {
	private static final Logger LOGGER = LogManager.getLogger(B1_RunHideEnrichGameLoad.class);
	private static int counter = 0;

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();

			new DerivedEnrichGameLoad().load(merge);
			LOGGER.info("found duplicates: " + counter);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class DerivedEnrichGameLoad extends MongoLoad<Game, Game> {

		public DerivedEnrichGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			String name = GeneralUtil.alphanumerify(game.name);
			String year = game.year;

			List<Game> games = GameUtil.getGames(true, true, name + " " + year,
					Filters.and(Filters.in("labels", name), Filters.eq("year", year)));

			if (games == null) {
				counter += 1;
			}

			return null;
		}
	}
}
