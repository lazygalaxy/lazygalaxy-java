package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

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

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			// further enrich the exiting roms with information from the latest mame/arcade
			// database
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
			List<Game> gameList = new ArrayList<Game>();

			String romId = GeneralUtil.alphanumerify(XMLUtil.getAttributeAsString(element, "name"));
			List<Game> games = GameUtil.getGames(false, false, romId, Filters.eq("romId", romId), Filters.in("systemId",
					GameSystem.ARCADE, GameSystem.ATOMISWAVE, GameSystem.DAPHNE, GameSystem.NAOMI, GameSystem.NEOGEO));

			if (games != null) {
				for (Game game : games) {
					gameList.addAll(process(game, element));
				}
			}

			return gameList;
		}

		private List<Game> process(Game game, Element element) throws Exception {
			List<Game> allGamesToReturn = new ArrayList<Game>();
			allGamesToReturn.add(game);

			String name = XMLUtil.getTagAsString(element, "description", 0);
			String year = XMLUtil.getTagAsString(element, "year", 0);
			Integer players = XMLUtil.getTagAttributeAsInteger(element, "input", "players", 0);
			String developer = XMLUtil.getTagAsString(element, "manufacturer", 0);

			game.mameGameInfo = new GameInfo(null, name, year, null, null, null, null, null, null, players, developer,
					null, null);

			game.cloneOfRomId = XMLUtil.getAttributeAsString(element, "cloneof");
			game.sourceFile = XMLUtil.getAttributeAsString(element, "sourcefile");
			game.sampleOf = XMLUtil.getAttributeAsString(element, "sampleof");
			game.status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);

			Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
			if (rotate == 90 || rotate == 270) {
				game.isVeritcal = true;
			} else {
				game.isVeritcal = false;
			}

			game.coins = XMLUtil.getTagAttributeAsInteger(element, "input", "coins", 0);
			game.input = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
			game.buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);
			if (game.buttons == null) {
				game.buttons = 0;
			}

			if (!StringUtils.isBlank(game.cloneOfRomId)) {
				List<Game> parentGames = GameUtil.getGames(false, false, game.cloneOfRomId,
						Filters.eq("romId", GeneralUtil.alphanumerify(game.cloneOfRomId)),
						Filters.in("systemId", GameSystem.ARCADE, GameSystem.ATOMISWAVE, GameSystem.DAPHNE,
								GameSystem.NAOMI, GameSystem.NEOGEO));

				if (parentGames != null) {
					game.parentMissing = false;
					for (Game parentGame : parentGames) {
						if (!SetUtil.contains(parentGame.clones, game.romId)) {
							parentGame.clones = SetUtil.addValue(parentGame.clones, game.romId);
							allGamesToReturn.add(parentGame);
						}
					}
				} else {
					game.parentMissing = true;
				}
			}

			return allGamesToReturn;
		}
	}
}
