package com.lazygalaxy.game.main_old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class A2_ArcadeEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(A2_ArcadeEnrichGameLoad.class);

//	public static void main(String[] args) throws Exception {
//		try {
//			GameMerge merge = new GameMerge();
//
//			// further enrich the exiting roms with information from the latest mame/arcade
//			// database
//			new ArcadeEnrichGameLoad().load("xml/mame240.xml", "machine", merge);
//			LOGGER.info("mame enrich game completed!");
//
//		} finally {
//			MongoConnectionHelper.INSTANCE.close();
//		}
//	}
//
//	private static class ArcadeEnrichGameLoad extends XMLLoad<Game> {
//
//		public ArcadeEnrichGameLoad() throws Exception {
//			super(Game.class);
//		}
//
//		@Override
//		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
//			String rom = XMLUtil.getAttributeAsString(element, "name");
//
//			Game game = MongoHelper.getHelper(Game.class).getDocumentById(rom + "_arcade");
//
//			if (game != null) {
//				String originalName = XMLUtil.getTagAsString(element, "description", 0);
//
//				game.name = GameUtil.pretify(GeneralUtil.split(GameUtil.pretify(originalName), "/")[0]);
//				game.addName(originalName);
//
//				if (game.labels == null) {
//					game.labels = new HashSet<>();
//				}
//				game.addLabel(game.name);
//
//				game.cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");
//				game.sourceFile = XMLUtil.getAttributeAsString(element, "sourcefile");
//				game.romOf = XMLUtil.getAttributeAsString(element, "romof");
//				game.sampleOf = XMLUtil.getAttributeAsString(element, "sampleof");
//				game.isMechanical = XMLUtil.getAttributeAsBoolean(element, "ismechanical");
//
//				game.year = XMLUtil.getTagAsString(element, "year", 0);
//				game.status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
//				game.manufacturers = XMLUtil.getTagAsStringSet(element, "manufacturer", "/");
//
//				Integer rotate = XMLUtil.getTagAttributeAsInteger(element, "display", "rotate", 0);
//				if (rotate == 0 || rotate == 180) {
//					game.isVeritcal = false;
//				} else {
//					game.isVeritcal = true;
//				}
//
//				game.players = XMLUtil.getTagAttributeAsInteger(element, "input", "players", 0);
//				game.coins = XMLUtil.getTagAttributeAsInteger(element, "input", "coins", 0);
//				game.input = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
//				game.buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);
//				if (game.buttons == null) {
//					game.buttons = 0;
//				}
//
//				if (!StringUtils.isBlank(game.cloneOf)) {
//					Game parentGame = MongoHelper.getHelper(Game.class).getDocumentById(game.cloneOf + "_arcade");
//					if (parentGame != null && !parentGame.containsClone(game.rom)) {
//						parentGame.addClone(game.rom);
//						return Arrays.asList(game, parentGame);
//					}
//				}
//				return Arrays.asList(game);
//			}
//
//			return null;
//		}
//	}
}
