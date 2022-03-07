package com.lazygalaxy.game.main_old;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

public class B1_RunHideEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B1_RunHideEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new DerivedHideEnrichGameLoad().load(merge);
			LOGGER.info("derived enrich completed!");

			// new CustomHideEnrichGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			// LOGGER.info("hide enrich completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class DerivedHideEnrichGameLoad extends MongoLoad<Game, Game> {

		public DerivedHideEnrichGameLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			if (!StringUtils.isBlank(game.name)) {
				if (game.cloneOf != null || (game.sampleOf != null && !StringUtils.equals(game.romId, game.sampleOf))) {
					game.hide = true;
				} else {
					game.hide = false;
				}

				switch (game.sourceFile) {
				case "decocass.cpp":
				case "megatech.cpp":
				case "playch10.cpp":
					game.hide = true;
				}
			} else {
				if (game.hide == null || !game.hide) {
					game.hide = true;
				} else {
					return null;
				}
			}
			return Arrays.asList(game);
		}
	}

	private static class CustomHideEnrichGameLoad extends TextFileLoad<Game> {

		public CustomHideEnrichGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String rom) throws Exception {
			Game game = MongoHelper.getHelper(Game.class).getDocumentById(rom + "_arcade");

			if (game == null) {
				LOGGER.info("No game found: " + rom);
			} else {
				game.hide = true;
				return Arrays.asList(game);
			}

			return null;
		}
	}
}
