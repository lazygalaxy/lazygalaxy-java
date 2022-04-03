package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MapOfSetDocumentoad;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
import com.mongodb.client.model.Filters;

public class E2_RunDeriveEnrichLoad {
	private static final Logger LOGGER = LogManager.getLogger(E2_RunDeriveEnrichLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new DeriveSourceEnrichLoad().load(merge);
			LOGGER.info("derive source enrich completed!");

			new FamilyDeriveEnrichLoad().load(merge, getMapObject());
			LOGGER.info("family derive enrich completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class DeriveSourceEnrichLoad extends MongoLoad<Game, Game> {

		public DeriveSourceEnrichLoad() throws Exception {
			super(Game.class, Game.class);

		}

		@Override
		protected List<Game> getMongoDocument(Game game) throws Exception {

			setField(game, "names");
			setField(game, "year");
			setField(game, "players");
			setField(game, "description");
			game.developer = null;
			game.publisher = null;
			setField(game, "manufacturers");

			return Arrays.asList(game);
		}

		private void setField(Game game, String field) throws Exception {
			for (String gameSource : GameSource.ALL) {
				GameInfo gameInfoObject = (GameInfo) Game.class.getField(gameSource + "GameInfo").get(game);
				if (gameInfoObject != null) {
					Object fieldObject = getField(gameInfoObject, field);
					if (fieldObject != null) {
						if (StringUtils.equals(field, "names")) {
							game.name = Lists.newArrayList((Set<String>) fieldObject).get(0);
							return;
						} else if (StringUtils.equals(field, "manufacturers")) {
							Set<String> manufacturerSet = (Set<String>) fieldObject;
							List<String> manufacturers = Lists
									.newArrayList(manufacturerSet.toArray(new String[manufacturerSet.size()]));

							if (game.developer == null) {
								game.developer = manufacturers.get(0);
								game.publisher = manufacturers.get(0);
							}

							for (String manufacturer : manufacturers) {
								if (!StringUtils.equals(game.publisher, manufacturer)) {
									game.publisher = manufacturer;
									return;
								}
							}
						} else {
							Game.class.getField(field).set(game, fieldObject);
							return;
						}
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

	private static Map<String, Set<Game>> getMapObject() throws Exception {
		Map<String, Set<Game>> mameGameByNameSetMap = new HashMap<String, Set<Game>>();

		List<Game> games = GameUtil.getGames(false, false, null, Filters.in("systemId", GameSystem.MAME));
		for (Game game : games) {
			for (String name : game.labels) {
				Set<Game> gameList = mameGameByNameSetMap.get(name);
				if (gameList == null) {
					gameList = new HashSet<Game>();
					mameGameByNameSetMap.put(name, gameList);
				}
				gameList.add(game);
			}
			if (game.family != null) {
				for (String name : game.family) {
					Set<Game> gamSet = mameGameByNameSetMap.get(name);
					if (gamSet == null) {
						gamSet = new HashSet<Game>();
						mameGameByNameSetMap.put(name, gamSet);
					}
					gamSet.add(game);
				}
			}
		}

		return mameGameByNameSetMap;
	}

	private static class FamilyDeriveEnrichLoad extends MapOfSetDocumentoad<Game> {

		public FamilyDeriveEnrichLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected Collection<Game> getMongoDocument(String key, Set<Game> storedDocument) throws Exception {
			if (storedDocument.size() > 1) {
				for (Game game1 : storedDocument) {
					for (Game game2 : storedDocument) {
						game1.family = SetUtil.addValueToTreeSet(game1.family, game2.gameId);
						if (game2.family != null) {
							game1.family = SetUtil.addValueToTreeSet(game1.family,
									game2.family.toArray(new String[game2.family.size()]));
						}
					}
				}
				return storedDocument;
			}
			return null;
		}
	}
}
