package main.load.other;

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

public class A3_RunOtherCoinOpsRomLoad {

    private static final Logger LOGGER = LogManager.getLogger(A3_RunOtherCoinOpsRomLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            for (String coinopsVersion : CoinOpsVersion.ALL) {
                for (String computerSystem : GameSystem.COMPUTER) {
                    new RomSetLoad(computerSystem, coinopsVersion)
                            .load("list/coinops/" + coinopsVersion + "/" + computerSystem + "_roms.ls", 0, merge);
                    LOGGER.info(coinopsVersion + " " + computerSystem + " rom list completed!");
                }

                for (String consoleSystem : GameSystem.CONSOLE) {
                    new RomSetLoad(consoleSystem, coinopsVersion)
                            .load("list/coinops/" + coinopsVersion + "/" + consoleSystem + "_roms.ls", 0, merge);
                    LOGGER.info(coinopsVersion + " " + consoleSystem + " rom list completed!");
                }

                for (String handheldSystem : GameSystem.HANDHELD) {
                    new RomSetLoad(handheldSystem, coinopsVersion)
                            .load("list/coinops/" + coinopsVersion + "/" + handheldSystem + "_roms.ls", 0, merge);
                    LOGGER.info(coinopsVersion + " " + handheldSystem + " rom list completed!");
                }
            }
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
                    game.coinopsGameInfo = new GameInfo(gameId, null);
                    game.coinopsGameInfo.originalName = gameInfoStatic.originalName;
                }
            }

            return games;
        }
    }

}
