package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.GameUtil;

public class A_RunMameGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(A_RunMameGameLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Game> merge = new FieldMerge<Game>();

			new MameGameLoad().load("xml/mame229.xml", "machine", merge);
			LOGGER.info("game load completed!");

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
				Boolean isBios = XMLUtil.getAttributeAsBoolean(element, "isbios");
				Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
				boolean hasInput = XMLUtil.containsTag(element, "input");

				// we only want to store high quality games with complete info
				if ((isBios == null || !isBios) && rotate != null && hasInput) {

					Set<String> extraInfo = new TreeSet<>();
					String prettyDescription = GameUtil.pretify(XMLUtil.getTagAsString(element, "description", 0),
							extraInfo);
					String[] descriptionSplit = GeneralUtil.split(prettyDescription, "/");

					game = new Game(rom + "_mame", GameUtil.pretify(descriptionSplit[0], extraInfo));
					game.addLabel(rom);
					game.rom = rom;

					game.sourceFile = XMLUtil.getAttributeAsString(element, "sourcefile");
					game.romOf = XMLUtil.getAttributeAsString(element, "romof");
					game.isMechanical = XMLUtil.getAttributeAsBoolean(element, "ismechanical");

					for (int i = 1; i < descriptionSplit.length; i++) {
						extraInfo.add(descriptionSplit[i]);
					}
					if (!extraInfo.isEmpty()) {
						game.extraInfo = new TreeSet<String>();
						game.extraInfo.addAll(extraInfo);
					}

					game.year = XMLUtil.getTagAsString(element, "year", 0);
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
					return Arrays.asList(game);
				}
			} else {
				game = MongoHelper.getHelper(Game.class).getDocumentById(cloneOf + "_mame");
				if (game != null) {
					if (game.clones == null) {
						game.clones = new TreeSet<String>();
					}
					if (!game.clones.contains(rom)) {
						game.clones.add(rom);
						return Arrays.asList(game);
					}
				}
			}

			return null;
		}
	}
}
