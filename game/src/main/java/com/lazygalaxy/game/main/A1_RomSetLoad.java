package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.Constant.RomSet;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.merge.GameMerge;

public class A1_RomSetLoad {

	private static final Logger LOGGER = LogManager.getLogger(A1_RomSetLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			new RomSetLoad(RomSet.MAME2003).load("txt/mame_2003_roms.txt", 0, merge);
			LOGGER.info("mame 2003 romset load completed!");

			new RomSetLoad(RomSet.MAME2003B).load("txt/mame_2003b_roms.txt", 0, merge);
			LOGGER.info("mame 2003b rom set load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class RomSetLoad extends TextFileLoad<Game> {
		final private String romSet;

		public RomSetLoad(String romSet) throws Exception {
			super(Game.class);
			this.romSet = romSet;
		}

		@Override
		protected List<Game> getMongoDocument(String line) throws Exception {
			String file = StringUtils.substring(line, line.lastIndexOf(" ") + 1);
			String rom = StringUtils.left(file, file.length() - 4);

			Game game = new Game(rom + "_arcade");
			game.rom = rom;
			game.addRomSet(romSet);
			game.systemId = GameSystem.ARCADE;

			return Arrays.asList(game);
		}
	}

}
