package main.load.console;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.CoinOpsVersion;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class A3_RunConsoleCoinOpsRomLoad {

    private static final Logger LOGGER = LogManager.getLogger(A3_RunConsoleCoinOpsRomLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            // Player 2 Legends
            new RomSetLoad(GameSystem.MEGADRIVE, CoinOpsVersion.PLAYER_LEGENDS_2)
                    .load("list/coinops/playerlegends2/megadrive_roms.ls", 0, merge);
            LOGGER.info("Player 2 Legends megadrive rom list completed!");

            new RomSetLoad(GameSystem.SNES, CoinOpsVersion.PLAYER_LEGENDS_2)
                    .load("list/coinops/playerlegends2/snes_roms.ls", 0, merge);
            LOGGER.info("Player 2 Legends snes rom list completed!");

            // Retro Aracde 2 Elites
            new RomSetLoad(GameSystem.MEGADRIVE, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/megadrive_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites megadrive rom list completed!");

            new RomSetLoad(GameSystem.SNES, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/snes_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites snes rom list completed!");

            new RomSetLoad(GameSystem.N64, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/n64_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites n64 rom list completed!");

            new RomSetLoad(GameSystem.PC, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/pc_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites pc rom list completed!");

            new RomSetLoad(GameSystem.PSP, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/psp_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites psp rom list completed!");

            new RomSetLoad(GameSystem.PSX, CoinOpsVersion.RETRO_ARCADE_2_ELITES)
                    .load("list/coinops/retroarcade2elites/psx_roms.ls", 0, merge);
            LOGGER.info("Retro Arcade 2 Elites psx rom list completed!");
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }

    private static class RomSetLoad extends LinuxListLoad<Game> {
        private String systemId;
        private String coinopsVersion;

        public RomSetLoad(String systemId, String coinopsVersion) throws Exception {
            super(Game.class);
            this.systemId = systemId;
            this.coinopsVersion = coinopsVersion;
        }

        @Override
        protected List<Game> getMongoDocumentByList(String file, long fileSize) throws Exception {
            String gameId = StringUtils.substring(file, 0, StringUtils.lastIndexOf(file, "."));

            gameInfoStatic.originalName = gameId;
            GameUtil.pretifyName(gameInfoStatic);
            String name = IterableUtils.get(gameInfoStatic.names, 0);

            List<Game> games = null;
            String querySystemId = GameSystem.MAME.contains(systemId) ? GameSystem.ARCADE : systemId;
            if (gameInfoStatic.version != null) {
                gameId = GeneralUtil.alphanumerify(name + gameInfoStatic.version);
                games = GameUtil.getGames(false, false, gameId, null, Filters.in("labels", gameId),
                        Filters.in("systemId", querySystemId));

                if (games == null && StringUtils.endsWith(name, " 1")) {
                    gameId = GeneralUtil.alphanumerify(name.substring(0, name.length() - 2) + gameInfoStatic.version);
                    games = GameUtil.getGames(false, false, gameId, null, Filters.in("labels", gameId),
                            Filters.in("systemId", querySystemId));
                }
            }

            if (games == null) {
                gameId = GeneralUtil.alphanumerify(name);
                games = GameUtil.getGames(false, false, gameId, null, Filters.in("labels", gameId),
                        Filters.in("systemId", querySystemId));

                if (games == null && StringUtils.endsWith(name, " 1")) {
                    gameId = GeneralUtil.alphanumerify(name.substring(0, name.length() - 2));
                    games = GameUtil.getGames(false, false, gameId, null, Filters.in("labels", gameId),
                            Filters.in("systemId", querySystemId));
                }
            }

            if (games == null) {
                LOGGER.warn(name + " not found for " + querySystemId);
            } else {
                for (Game game : games) {
                    game.coinopsVersions = SetUtil.addValueToTreeSet(game.coinopsVersions, coinopsVersion);
                }
            }

            return games;
        }
    }

}
