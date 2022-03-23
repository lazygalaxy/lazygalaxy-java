package com.lazygalaxy.game.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;

public class GameUtil {
	private static final Logger LOGGER = LogManager.getLogger(GameUtil.class);

	public static void pretifyName(GameInfo info) {
		if (!StringUtils.isBlank(info.originalName)) {
			info.name = info.originalName;
			info.version = null;

			int index = Integer.MAX_VALUE;
			if (StringUtils.contains(info.name, ")")) {
				index = Math.min(index, info.name.indexOf("("));
			}

			if (StringUtils.contains(info.name, "]")) {
				index = Math.min(index, info.name.indexOf("["));
			}

			if (index != Integer.MAX_VALUE) {
				info.version = StringUtils.substring(info.name, index, info.name.length());
				info.name = StringUtils.substring(info.name, 0, index);
			}

			info.name = pretify(info.name);
			info.version = pretify(info.version);
		} else {
			info.originalName = null;
			info.name = null;
			info.version = null;
		}
	}

	private static String pretify(String str) {
		if (!StringUtils.isBlank(str)) {
			// str = str.replaceAll("\n", " ");
			// str = str.replaceAll("\r", " ");
			// str = str.replaceAll("\\.", ". ");
			str = str.replaceAll("&amp;", "&");
			str = str.replaceAll("\\)", " ");
			str = str.replaceAll("\\(", " ");
			str = str.replaceAll("\\]", " ");
			str = str.replaceAll("\\[", " ");

			str = str.replaceAll(" - ", " : ");
			str = str.replaceAll("- ", ": ");
			str = str.replaceAll(" -", " :");

			while (StringUtils.contains(str, " :")) {
				str = str.replaceAll(" :", ":");
			}
			str = str.replaceAll(":", ": ");
			while (StringUtils.contains(str, "  ")) {
				str = str.replaceAll("  ", " ");
			}

			// numbers
			str = str.replaceAll(" I:", " 1:");
			str = str.replaceAll(" II:", " 2:");
			str = str.replaceAll(" III:", " 3:");
			str = str.replaceAll(" IV:", " 4:");
			str = str.replaceAll(" V:", " 5:");
			str = str.replaceAll(" VI:", " 6:");
			str = str.replaceAll(" VII:", " 7:");
			str = str.replaceAll(" VIII:", " 8:");
			str = str.replaceAll(" IX:", " 9:");
			str = str.replaceAll(" X:", " 10:");
			str = str.replaceAll(" XI:", " 11:");
			str = str.replaceAll(" XII:", " 12:");
			str = str.replaceAll(" XIII:", " 13:");
			str = str.replaceAll(" XIV:", " 14:");
			str = str.replaceAll(" XV:", " 15:");
			str = str.replaceAll(" XVI:", " 16:");
			str = str.replaceAll(" XVII:", " 17:");
			str = str.replaceAll(" XVIII:", " 18:");
			str = str.replaceAll(" XIX:", " 19:");
			str = str.replaceAll(" XX:", " 20:");

			str = str.trim();
			return str;
		}
		return null;
	}

	public static List<Game> getGames(boolean printNoGameFound, boolean printMultipleGamesFound, String identifier,
			Bson... filters) throws Exception {
		List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(filters);

		if (games.size() == 0) {
			if (printNoGameFound) {
				LOGGER.warn("game not found: " + identifier);
			}
			return null;
		} else if (games.size() > 1) {
			int unhiddenCounter = 0;
			String gameIds = "";
			for (Game game : games) {
				gameIds += " " + game.id;
			}

			if (unhiddenCounter > 1) {
				if (printMultipleGamesFound) {
					LOGGER.warn("multiple games found: " + identifier + " with roms: " + gameIds);
				}
				return null;
			}
		}

		return games;
	}
}
