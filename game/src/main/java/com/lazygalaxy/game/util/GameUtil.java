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
			String processName = info.originalName;
			info.version = null;
			info.names = null;

			int index = Integer.MAX_VALUE;
			if (StringUtils.contains(processName, ")")) {
				index = Math.min(index, processName.indexOf("("));
			}

			if (StringUtils.contains(processName, "]")) {
				index = Math.min(index, processName.indexOf("["));
			}

			if (index != Integer.MAX_VALUE) {
				info.version = StringUtils.substring(processName, index, processName.length());
				processName = StringUtils.substring(processName, 0, index);
			}

			for (String name : StringUtils.splitByWholeSeparator(processName, " / ")) {
				name = pretify(name);
				if (StringUtils.contains(name, ": ")) {
					String uniqueNameBefore = StringUtils.substring(name, 0, name.lastIndexOf(": "));
					String uniqueNameAfter = uniqueNameBefore.replaceAll(": ", " ");
					if (StringUtils.startsWith(uniqueNameAfter.toLowerCase(), "the ")) {
						uniqueNameAfter = StringUtils.right(uniqueNameAfter, uniqueNameAfter.length() - 4) + ", The";
					}
					name = name.replaceAll(uniqueNameBefore, uniqueNameAfter);
					info.names = SetUtil.addValueToArrayList(info.names, name);
					info.names = SetUtil.addValueToArrayList(info.names, uniqueNameAfter);
					if (StringUtils.endsWith(uniqueNameAfter.toLowerCase(), ", the")) {
						String smallerName = StringUtils.left(uniqueNameAfter, uniqueNameAfter.length() - 5);
						info.names = SetUtil.addValueToArrayList(info.names,
								smallerName + StringUtils.substring(name, name.lastIndexOf(": "), name.length()));
						info.names = SetUtil.addValueToArrayList(info.names, smallerName);
					}
				} else {
					if (StringUtils.startsWith(name.toLowerCase(), "the ")) {
						name = StringUtils.right(name, name.length() - 4) + ", The";
					}
					info.names = SetUtil.addValueToArrayList(info.names, name);
					if (StringUtils.endsWith(name.toLowerCase(), ", the")) {
						info.names = SetUtil.addValueToArrayList(info.names, StringUtils.left(name, name.length() - 5));
					}
				}
			}
			info.version = pretify(info.version);

		} else {
			info.originalName = null;
			info.names = null;
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
			str = str.replaceAll("':", ":");

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

			str = str.trim();
			str = str.replaceAll(" I$", " 1");
			str = str.replaceAll(" II$", " 2");
			str = str.replaceAll(" III$", " 3");
			str = str.replaceAll(" IV$", " 4");
			str = str.replaceAll(" V$", " 5");
			str = str.replaceAll(" VI$", " 6");
			str = str.replaceAll(" VII$", " 7");
			str = str.replaceAll(" VIII$", " 8");
			str = str.replaceAll(" IX$", " 9");

			str = str.trim();
			return str;
		}
		return null;
	}

	public static List<Game> getGames(boolean printNoGameFound, boolean printMultipleGamesFound, String identifier,
			Bson sort, Bson... find) throws Exception {
		List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(sort, find);

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
