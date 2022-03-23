package com.lazygalaxy.game.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XX_RomSetLoad {

	private static final Logger LOGGER = LogManager.getLogger(XX_RomSetLoad.class);

//	public static void main(String[] args) throws Exception {
//		try {
//			GameMerge merge = new GameMerge();
//
//			// initial entries are based on the romset for mame2003
//			new RomSetLoad(RomSet.MAME2003).load("txt/roms/mame2003_roms.txt", 0, merge);
//			LOGGER.info("mame2003 rom set load completed!");
//
//			// initial entries are based on the romset for fbneo
//			new RomSetLoad(RomSet.FBNEO).load("txt/roms/fbneo_roms.txt", 0, merge);
//			LOGGER.info("fbneo rom set load completed!");
//
//			// initial entries are based on the romset for fbneo
//			new RomSetLoad(RomSet.MAME2010).load("txt/roms/mame2010_roms.txt", 0, merge);
//			LOGGER.info("mame2010 rom set load completed!");
//		} finally {
//			MongoConnectionHelper.INSTANCE.close();
//		}
//	}
//
//	private static class RomSetLoad extends TextFileLoad<Game> {
//		final private String romSet;
//
//		public RomSetLoad(String romSet) throws Exception {
//			super(Game.class);
//			this.romSet = romSet;
//		}
//
//		@Override
//		protected List<Game> getMongoDocument(String line) throws Exception {
//			String file = StringUtils.substring(line, line.lastIndexOf(" ") + 1);
//			String rom = StringUtils.left(file, file.length() - 4);
//
//			// we know these are arcade romsets, so we adjust the id accordingly
//			Game game = new Game(rom + "_arcade");
//			game.rom = rom;
//			game.addRomSet(romSet);
//			game.system = GameSystem.ARCADE;
//
//			return Arrays.asList(game);
//		}
//	}

}
