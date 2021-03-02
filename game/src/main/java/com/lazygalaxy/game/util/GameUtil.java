package com.lazygalaxy.game.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.game.domain.Game;

public class GameUtil {
	private static final Logger LOGGER = LogManager.getLogger(GameUtil.class);

	public static String pretify(String str) {
		String result = str;
		if (result != null) {
			if (StringUtils.endsWith(result, ")")) {
				result = StringUtils.substring(result, 0, result.lastIndexOf("(")).trim();
			}

			result = result.replaceAll("\n", " ");
			result = result.replaceAll("\r", " ");
			result = result.replaceAll("'", "");
			result = result.replaceAll("\\.", ". ");
			result = result.replaceAll("&amp;", "&");

			result = result.replaceAll(" - ", " : ");
			result = result.replaceAll("- ", ": ");
			result = result.replaceAll(" -", " :");
			while (StringUtils.contains(result, " :")) {
				result = result.replaceAll(" :", ":");
			}
			result = result.replaceAll(":", ": ");
			while (StringUtils.contains(result, "  ")) {
				result = result.replaceAll("  ", " ");
			}
		}
		return result;
	}

	public static List<Game> getGames(boolean printNoGameFound, boolean multipleGamesFound, String identifier,
			Bson... filters) throws Exception {
		List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(filters);

		if (games.size() == 0) {
			if (printNoGameFound) {
				LOGGER.warn("game not found: " + identifier);
			}
			return null;
		} else if (games.size() > 1) {
			int unhiddenCounter = 0;
			for (Game game : games) {
				if (game.hide == null || !game.hide) {
					unhiddenCounter += 1;
				}
			}

			if (unhiddenCounter == 1) {
				// all good
			} else {
				if (multipleGamesFound) {
					LOGGER.warn("multiple games found: " + identifier);
				}
				return null;
			}
		}

		return games;
	}
}
