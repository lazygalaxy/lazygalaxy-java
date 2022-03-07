package com.lazygalaxy.game.main_old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class B2_RunVManEnrichGameLoad {
	private static final Logger LOGGER = LogManager.getLogger(B2_RunVManEnrichGameLoad.class);

//	public static void main(String[] args) throws Exception {
//		try {
//			GameMerge merge = new GameMerge();
//
//			new RetroArchGameLoad().load("xml/vman/retroarch_arcade_games.xml", "game", merge);
//			new RetroArchGameLoad().load("xml/vman/retroarch_atomiswave_games.xml", "game", merge);
//			new RetroArchGameLoad().load("xml/vman/retroarch_daphne_games.xml", "game", merge);
//			new RetroArchGameLoad().load("xml/vman/retroarch_naomi_games.xml", "game", merge);
//			new RetroArchGameLoad().load("xml/vman/retroarch_neogeo_games.xml", "game", merge);
//			LOGGER.info("vman enrich load completed!");
//
//		} finally {
//			MongoConnectionHelper.INSTANCE.close();
//		}
//	}
//
//	private static class RetroArchGameLoad extends XMLLoad<Game> {
//
//		public RetroArchGameLoad() throws Exception {
//			super(Game.class);
//		}
//
//		@Override
//		protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
//			String path = XMLUtil.getTagAsString(element, "path", 0);
//			String rom = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
//			rom = StringUtils.substring(rom, StringUtils.lastIndexOf(rom, "/") + 1, rom.length());
//
//			String image = XMLUtil.getTagAsString(element, "image", 0);
//			String alternativeRom = null;
//			if (image != null) {
//				alternativeRom = StringUtils.substring(image, 0, StringUtils.lastIndexOf(image, "."));
//				alternativeRom = StringUtils.substring(alternativeRom, StringUtils.lastIndexOf(alternativeRom, "/") + 1,
//						alternativeRom.length());
//			}
//			String name = GeneralUtil.alphanumerify(GameUtil.pretify(XMLUtil.getTagAsString(element, "name", 0)));
//			String year = StringUtils.left(XMLUtil.getTagAsString(element, "releasedate", 0), 4);
//
//			List<Game> games = null;
//
//			if (!StringUtils.isBlank(name) && !StringUtils.isBlank(year)) {
//				games = GameUtil.getGames(false, true, name + " " + year,
//						Filters.and(Filters.in("labels", name), Filters.eq("year", year)));
//			}
//
//			if (games == null && !StringUtils.isBlank(rom)) {
//				if (StringUtils.isBlank(alternativeRom)) {
//					alternativeRom = rom;
//				}
//				games = GameUtil.getGames(false, true, rom + " " + alternativeRom,
//						Filters.or(Filters.eq("rom", rom), Filters.eq("rom", alternativeRom), Filters.in("clones", rom),
//								Filters.in("clones", alternativeRom)));
//			}
//
//			if (games != null) {
//				for (Game game : games) {
//					game.description = GameUtil.pretify(XMLUtil.getTagAsString(element, "desc", 0));
//
//					String genreString = XMLUtil.getTagAsString(element, "genre", 0);
//					if (genreString != null) {
//						genreString = genreString.toLowerCase();
//						genreString = genreString.replaceAll("plateform", "platform");
//						genreString = genreString.replaceAll(" and ", " ");
//						String[] genreArray = GeneralUtil.split(genreString, "[/,-]");
//
//						// TODO: genre for sure needs more work to clean up
//						for (String token : genreArray) {
//							game.addGenre(token);
//
//							if (StringUtils.contains(token, Genre.SPORT)) {
//								game.addGenre(Genre.SPORT);
//							}
//							if (StringUtils.contains(token, Genre.BASKETBALL)) {
//								game.addGenre(Genre.BASKETBALL);
//							}
//							if (StringUtils.contains(token, Genre.LIGHTGUN)) {
//								game.addGenre(Genre.LIGHTGUN);
//							}
//						}
//					}
//				}
//
//				return games;
//			}
//			return null;
//		}
//	}
}
