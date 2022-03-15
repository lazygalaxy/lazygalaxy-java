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
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;

public class B2_RunDeriveEnrichLoad {
	private static final Logger LOGGER = LogManager.getLogger(B2_RunDeriveEnrichLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new DeriveEnrichLoad().load(merge);
			LOGGER.info("derive enrich completed!");

			new CustomHideGameLoad().load("hide/custom_hide.txt", 0, merge);
			LOGGER.info("custom hide game completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class DeriveEnrichLoad extends MongoLoad<Game, Game> {

		public DeriveEnrichLoad() throws Exception {
			super(Game.class, Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {
			if (game.cloneOfRomId != null && !game.parentMissing) {
				game.hide = true;
			} else {
				game.hide = false;
			}

			setField(game, "name");
			setField(game, "year");
			setField(game, "players");
			setField(game, "description");
			setField(game, "developer");
			setField(game, "publisher");

			return Arrays.asList(game);
		}

		private void setField(Game game, String field) throws Exception {
			for (String gameSource : GameSource.ALL) {
				Object gameInfoObject = Game.class.getField(gameSource + "GameInfo").get(game);
				if (gameInfoObject != null) {
					Object fieldObject = getField((GameInfo) gameInfoObject, field);
					if (fieldObject != null) {
						if (StringUtils.equals(field, "name")) {
							String[] nameArray = GeneralUtil.split(GameUtil.pretify((String) fieldObject), "/");
							for (int i = 0; i < nameArray.length; i++) {
								nameArray[i] = GameUtil.pretify(nameArray[i]);
								game.addLabel(nameArray[i]);
							}

							fieldObject = nameArray[0];
						}

						Game.class.getField(field).set(game, fieldObject);
						return;
					}
				}
			}
		}

		private Object getField(GameInfo gameInfo, String field) throws Exception {
			if (gameInfo != null) {
				Object fieldObject = GameInfo.class.getField(field).get(gameInfo);
				if (fieldObject != null) {
					return fieldObject;
				}
			}
			return null;
		}
	}

	private static class CustomHideGameLoad extends TextFileLoad<Game> {

		public CustomHideGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(String gameId) throws Exception {
			Game game = MongoHelper.getHelper(Game.class).getDocumentById(gameId);
			if (game != null) {
				game.hide = true;
				return Arrays.asList(game);
			} else {
				LOGGER.warn("game id not found:" + gameId);
			}
			return null;

		}
	}
}
