package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.GameSystem;
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
		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {

			String rom = XMLUtil.getAttributeAsString(element, "name");
			String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");
			Game game = null;
			if (cloneOf == null) {
				Boolean isMechanical = XMLUtil.getAttributeAsBoolean(element, "ismechanical");
				Boolean isBios = XMLUtil.getAttributeAsBoolean(element, "isbios");
				String year = GeneralUtil.numerify(XMLUtil.getTagAsString(element, "year", 0));
				Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
				boolean hasInput = XMLUtil.containsTag(element, "input");

				// we only want to store high quality games with complete info
				if ((isMechanical == null || !isMechanical) && (isBios == null || !isBios) && rotate != null
						&& hasInput) {

					String name = GeneralUtil
							.split(GameUtil.pretify(XMLUtil.getTagAsString(element, "description").get(0)), "/")[0];
					game = new Game(rom + "_mame", name);
					game.addLabel(rom);
					game.rom = rom;
					game.sourceFile = XMLUtil.getAttributeAsString(element, "sourcefile");
					game.romOf = XMLUtil.getAttributeAsString(element, "romof");

					switch (game.sourceFile) {
					case "naomi.cpp":
						game.systemId = GameSystem.NAOMI;
						break;
					case "neogeo.cpp":
						game.systemId = GameSystem.NEOGEO;
						break;
					case "playch10.cpp":
						game.systemId = GameSystem.PLAYCHOICE10;
						break;
					default:
						game.systemId = GameSystem.ARCADE;
					}

					if (StringUtils.length(year) == 4) {
						game.year = Integer.parseInt(year);
					}
					game.status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
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
				}
			} else {
				game = MongoHelper.getHelper(Game.class).getDocumentById(cloneOf + "_mame");
				if (game != null) {
					if (game.clones == null) {
						game.clones = new TreeSet<String>();
					}
					game.clones.add(rom);
				}
			}

			if (game != null) {
				return Arrays.asList(game);
			}

			return null;
		}
	}
}
