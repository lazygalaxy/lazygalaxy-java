package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.System;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.GameUtil;

public class A_RunMameGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(A_RunMameGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();
			new MameGameLoad().load("xml/mame229.xml", "machine", merge);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class MameGameLoad extends XMLLoad<Game> {

		public MameGameLoad() throws Exception {
			super(Game.class);
		}

		@Override
		protected Game getMongoDocument(Element element, List<String> extraTagValues) throws Exception {

			String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");
			String year = XMLUtil.getTagAsString(element, "year", 0);
			String status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
			Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
			boolean hasInput = XMLUtil.containsTag(element, "input");

			// we only want to store high quality games with complete info
			if (StringUtils.isBlank(cloneOf) && !StringUtils.contains(year, "?") && StringUtils.contains(status, "good")
					&& rotate != null && hasInput) {
				String rom = XMLUtil.getAttributeAsString(element, "name");
				String name = GameUtil.pretify(XMLUtil.getTagAsString(element, "description").get(0));

				Game game = new Game(rom + "_mame", name);
				game.addLabel(rom);
				game.rom = rom;
				game.romOf = XMLUtil.getAttributeAsString(element, "romof");
				if (StringUtils.isBlank(game.romOf)) {
					game.systemId = System.ARCADE;
				} else {
					switch (game.romOf) {
					case "awbios":
						game.systemId = System.ATOMISWAVE;
						break;
					case "naomi":
						game.systemId = System.NAOMI;
						break;
					case "neogeo":
						game.systemId = System.NEOGEO;
						break;
					}
				}
				game.year = Integer.parseInt(year);
				game.manufacturers = XMLUtil.getTagAsStringSet(element, "manufacturer", "/");

				if (rotate == 0 || rotate == 180) {
					game.isVeritcal = false;
				} else {
					game.isVeritcal = true;
				}

				game.players = XMLUtil.getTagAttributeAsInteger(element, "input", "players", 0);
				game.coins = XMLUtil.getTagAttributeAsInteger(element, "input", "coins", 0);
				game.input = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
				game.buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);

				return game;
			}

//			String path = XMLUtils.handleString(element, "path");
//			String romId = GeneralUtil
//					.alphanumerify(StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, ".")));
//			String systemId = GeneralUtil.alphanumerify(extraTagValues.get(0));
//
//			String name = XMLUtils.handleString(element, "name");
//			name = GameUtil.pretify(name);
//			Game game = new Game(romId + "_" + systemId, name);
//
//			game.addLabel(romId);
//
//			String image = XMLUtils.handleString(element, "image");
//			if (image != null) {
//				String alternativeId = StringUtils.substring(image, 0, StringUtils.lastIndexOf(image, "."));
//				alternativeId = StringUtils.substring(alternativeId, StringUtils.lastIndexOf(alternativeId, "/") + 1,
//						alternativeId.length());
//
//				if (!StringUtils.equals(alternativeId, romId)) {
//					game.addLabel(alternativeId);
//					game.alternativeId = alternativeId;
//				}
//			}
//
//			game.romId = romId;
//			game.systemId = systemId;
//			game.year = XMLUtils.handleInteger(element, "releasedate", 4);
//			game.developer = XMLUtils.handleString(element, "developer");
//			if (StringUtils.startsWith(game.developer, "Data East")) {
//				game.developer = "Data East";
//			}
//			game.publisher = XMLUtils.handleString(element, "publisher");
//			if (StringUtils.startsWith(game.publisher, "Data East")) {
//				game.publisher = "Data East";
//			}
//			game.description = GameUtil.pretify(XMLUtils.handleString(element, "desc"));
//
//			// genre
//			game.genre = new TreeSet<String>();
//			String genreString = XMLUtils.handleString(element, "genre");
//			if (genreString != null) {
//				genreString = genreString.toLowerCase();
//				genreString = genreString.replaceAll("plateform", "platform");
//				genreString = genreString.replaceAll(" and ", " ");
//				String[] genreArray = GeneralUtil.split(genreString, "[/,-]");
//
//				for (String token : genreArray) {
//					token = token.replace("swimming", Genre.POOL);
//					token = token.replace("sports", Genre.SPORT);
//					token = token.replace("lightgun shooter", Genre.LIGHTGUN);
//					game.addGenre(token);
//				}
//			}
//
//			// players
//			game.players = new TreeSet<Integer>();
//			String[] playerArray = XMLUtils.handleStringArray(element, "players", "-");
//			if (playerArray != null) {
//				if (playerArray.length == 1) {
//					game.players.add(Integer.parseInt(playerArray[0]));
//				} else if (playerArray.length == 2) {
//					for (int i = Integer.parseInt(playerArray[0]); i <= Integer.parseInt(playerArray[1]); i++) {
//						game.players.add(i);
//					}
//				} else {
//					throw new Exception("unexpected players " + XMLUtils.handleString(element, "players"));
//				}
//			} else {
//				game.players.add(1);
//			}
//
//			String collection = GeneralUtil.alphanumerify(extraTagValues.get(1));
//			game.collections = new TreeSet<String>();
//			game.collections.add(collection);
//
//			game.hide = XMLUtils.handleBoolean(element, "hide");
//
//			return game;

			return null;
		}
	}
}
