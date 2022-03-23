package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
import com.mongodb.client.model.Filters;

public class B1_MameGameInfoSourceLoad {
	private static final Logger LOGGER = LogManager.getLogger(B1_MameGameInfoSourceLoad.class);
	private static final GameInfo gameInfoStatic = new GameInfo();

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			// enrich the exiting roms with information from the latest mameatabase
			new MameGameInfoLoad().load("source/mame/mame240.xml", "machine", merge);
			LOGGER.info("mame enrich game completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class MameGameInfoLoad extends XMLLoad<Game> {

		public MameGameInfoLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String gameId = GeneralUtil.alphanumerify(XMLUtil.getAttributeAsString(element, "name"));
			List<Game> games = GameUtil.getGames(false, false, gameId, Filters.eq("gameId", gameId),
					Filters.in("systemId", GameSystem.MAME));
			Boolean isGuess = false;

			if (games == null || games.size() == 0) {
				String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");
				if (StringUtils.isBlank(cloneOf)) {
					isGuess = true;
					gameInfoStatic.originalName = XMLUtil.getTagAsString(element, "description", 0);
					GameUtil.pretifyName(gameInfoStatic);
					String name = GeneralUtil.alphanumerify(gameInfoStatic.name);

					games = GameUtil.getGames(false, false, gameId, Filters.in("labels", name),
							Filters.in("systemId", GameSystem.MAME));
				}
			}

			List<Game> gameList = new ArrayList<Game>();
			if (games != null && games.size() > 0) {
				for (Game game : games) {
					gameList.addAll(process(game, element, isGuess));
				}
			}

			return gameList;
		}

		private List<Game> process(Game game, Element element, Boolean isGuess) throws Exception {
			List<Game> allGamesToReturn = new ArrayList<Game>();
			allGamesToReturn.add(game);

			String originalName = XMLUtil.getTagAsString(element, "description", 0);
			String year = XMLUtil.getTagAsString(element, "year", 0);
			String players = XMLUtil.getTagAttributeAsString(element, "input", "players", 0);
			String developer = XMLUtil.getTagAsString(element, "manufacturer", 0);
			Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
			Boolean isVertical = false;
			if (rotate != null && (rotate == 90 || rotate == 270)) {
				isVertical = true;
			}
			Set<String> inputs = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
			Integer buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);
			String status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
			String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");

			game.mameGameInfo = new GameInfo(originalName, year, players, developer, isVertical, inputs, buttons,
					status, isGuess);
			GameUtil.pretifyName(game.mameGameInfo);

			if (!StringUtils.isBlank(cloneOf)) {
				String cloneOfGameId = GeneralUtil.alphanumerify(cloneOf);
				List<Game> parentGames = GameUtil.getGames(false, false, cloneOf, Filters.eq("gameId", cloneOfGameId),
						Filters.in("systemId", GameSystem.MAME));

				if (parentGames != null) {
					for (Game parentGame : parentGames) {
						if (!SetUtil.contains(parentGame.family, game.gameId)) {
							parentGame.family = SetUtil.addValue(parentGame.family, parentGame.gameId);
							parentGame.family = SetUtil.addValue(parentGame.family, game.gameId);

							game.family = SetUtil.addValue(game.family, parentGame.gameId);
							game.family = SetUtil.addValue(game.family, game.gameId);

							allGamesToReturn.add(parentGame);
						}
					}
				}
			}

			return allGamesToReturn;
		}
	}
}
