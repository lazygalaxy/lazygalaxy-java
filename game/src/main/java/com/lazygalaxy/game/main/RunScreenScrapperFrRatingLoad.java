package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.XMLUtils;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Ratings;

public class RunScreenScrapperFrRatingLoad {

	private static final Logger LOGGER = LogManager.getLogger(RunScreenScrapperFrRatingLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new ScrapperFrRatingLoad().load("xml/mame_game.xml", "game", "System");
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class ScrapperFrRatingLoad extends XMLLoad<Ratings> {

		public ScrapperFrRatingLoad() throws Exception {
			super(Ratings.class);
		}

		@Override
		protected Ratings getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String system = extraTagValues.get(0);
			String path = XMLUtils.handleString(element, "path");
			Double rating = XMLUtils.handleDouble(element, "rating");

			Game game = MongoHelper.getHelper(Game.class).getDocumentById(system + path);
			if (game == null) {
				throw new Exception("game not found: " + system + " " + path);
			}

			return new Ratings(game.id, rating, null);
		}
	}

}
