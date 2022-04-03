package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class B2_CoinOpsGameInfoEnrichLoad {

	private static final Logger LOGGER = LogManager.getLogger(B2_CoinOpsGameInfoEnrichLoad.class);
	private static final GameInfo gameInfoStatic = new GameInfo();

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			// Player 2 Legends
			new RomSetLoad(GameSystem.ARCADE, GameSource.PLAYER_LEGENDS_2)
					.load("list/coinops/playerlegends2/arcade_roms.ls", 0, merge);
			LOGGER.info("Player 2 Legends arcade rom list completed!");

			new RomSetLoad(GameSystem.MEGADRIVE, GameSource.PLAYER_LEGENDS_2)
					.load("list/coinops/playerlegends2/megadrive_roms.ls", 0, merge);
			LOGGER.info("Player 2 Legends megadrive rom list completed!");

			new RomSetLoad(GameSystem.SNES, GameSource.PLAYER_LEGENDS_2)
					.load("list/coinops/playerlegends2/snes_roms.ls", 0, merge);
			LOGGER.info("Player 2 Legends snes rom list completed!");

			// Retro Aracde 2 Elites
			new RomSetLoad(GameSystem.ARCADE, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/arcade_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites arcade rom list completed!");

			new RomSetLoad(GameSystem.MEGADRIVE, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/megadrive_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites megadrive rom list completed!");

			new RomSetLoad(GameSystem.SNES, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/snes_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites arcade rom list completed!");

			new RomSetLoad(GameSystem.N64, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/n64_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites n64 rom list completed!");

			new RomSetLoad(GameSystem.PSP, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/psp_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites psp rom list completed!");

			new RomSetLoad(GameSystem.PSX, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/psx_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites psx rom list completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class RomSetLoad extends LinuxListLoad<Game> {
		private String systemId;
		private String source;

		public RomSetLoad(String systemId, String source) throws Exception {
			super(Game.class);
			this.systemId = systemId;
			this.source = source;
		}

		@Override
		protected List<Game> getMongoDocumentByList(String file, long fileSize) throws Exception {
			String gameId = StringUtils.substring(file, 0, StringUtils.lastIndexOf(file, "."));

			List<Game> games = null;
			if (GameSystem.MAME.contains(systemId)) {

				games = GameUtil.getGames(false, true, gameId, Filters.in("labels", gameId),
						Filters.in("systemId", GameSystem.MAME));
			}

			if (games == null) {
				gameInfoStatic.originalName = gameId;
				GameUtil.pretifyName(gameInfoStatic);
				games = GameUtil.getGames(true, true, gameId,
						Filters.in("labels", GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0))),
						Filters.in("systemId", GameSystem.MAME));
			}

//			switch (systemId) {
//			case GameSystem.ARCADE:
//				game = new Game(systemId, GeneralUtil.alphanumerify(gameId));
//				Game.class.getField(source + "GameInfo").set(game, new GameInfo(game.gameId, null, fileSize));
//				break;
//			default:
//				gameInfoStatic.originalName = gameId;
//				GameUtil.pretifyName(gameInfoStatic);
//				game = new Game(systemId, GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0)));
//				Game.class.getField(source + "GameInfo").set(game, new GameInfo(game.gameId, gameId, fileSize));
//				break;
//			}
//
//			GameUtil.pretifyName((GameInfo) Game.class.getField(source + "GameInfo").get(game));
//			game.addLabel(game.gameId);
//			return Arrays.asList(game);

			return null;
		}
	}

}
