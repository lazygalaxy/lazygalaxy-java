package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.game.Collection;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

//{$and:[{name:{$ne:null}},{hide:{$eq:null}}]}

public class B1_RunCustomEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B1_RunCustomEnrichGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new DerivedEnrichGameLoad().load(merge);
			LOGGER.info("derived enrich completed!");

			new HideEnrichGameLoad().load("txt/custom_hide_games.txt", 0, merge);
			LOGGER.info("hide enrich completed!");
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
			if (!StringUtils.isBlank(game.name)) {
				if (game.cloneOf != null) {
					game.hide = true;
				} else {
					game.hide = false;
				}

				switch (game.sourceFile) {
				case "naomi.cpp":
					game.systemId = GameSystem.NAOMI;
					break;
				case "neogeo.cpp":
					game.systemId = GameSystem.NEOGEO;
					break;
				case "megatech.cpp":
				case "playch10.cpp":
					game.hide = true;
				default:
					game.systemId = GameSystem.ARCADE;
				}

				game.collections = Collection.get(game);
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

	private static class HideEnrichGameLoad extends TextFileLoad<Game> {

		public HideEnrichGameLoad() throws Exception {
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
