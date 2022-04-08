package com.lazygalaxy.game.main;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;

public class A3_CoinOpsSourceLoad {

	private static final Logger LOGGER = LogManager.getLogger(A3_CoinOpsSourceLoad.class);
	private static final GameInfo gameInfoStatic = new GameInfo();

	public static void main(String[] args) throws Exception {
		try {
			GameMerge merge = new GameMerge();

			// Player 2 Legends
			new RomSetLoad(GameSystem.ARCADE, GameSource.PLAYER_LEGENDS_2)
					.load("list/coinops/playerlegends2/arcade_roms.ls", 0, merge);
			LOGGER.info("Player 2 Legends arcade rom list completed!");

			// Retro Aracde 2 Elites
			new RomSetLoad(GameSystem.ARCADE, GameSource.RETRO_ARCADE_2_ELITES)
					.load("list/coinops/retroarcade2elites/arcade_roms.ls", 0, merge);
			LOGGER.info("Retro Arcade 2 Elites arcade rom list completed!");
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

			gameInfoStatic.originalName = gameId;
			GameUtil.pretifyName(gameInfoStatic);

			Game game = null;
			String querySystemId = GameSystem.MAME.contains(systemId) ? GameSystem.ARCADE : systemId;
			if (gameInfoStatic.version == null) {
				gameId = GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0));
			} else {
				gameId = GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0) + gameInfoStatic.version);
			}

			game = MongoHelper.getHelper(Game.class).getDocumentById(querySystemId + "_" + gameId);
			if (game == null) {
				game = new Game(querySystemId, gameId);
			}

			Game.class.getField(source + "GameInfo").set(game, new GameInfo(game.gameId, null, fileSize));

			GameUtil.pretifyName((GameInfo) Game.class.getField(source + "GameInfo").get(game));
			game.addLabel(game.gameId);
			return Arrays.asList(game);
		}
	}

}
