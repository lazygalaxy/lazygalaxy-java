package main.load.arcade;

import com.lazygalaxy.common.util.SetUtil;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.CoinOpsVersion;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class A4_RunArcadeCoinOpsRomLoad {

    private static final Logger LOGGER = LogManager.getLogger(A4_RunArcadeCoinOpsRomLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            for (String coinopsVersion : CoinOpsVersion.ALL) {
                for (String subSystemId : GameSystem.MAME) {
                    if (new RomSetLoad(GameSystem.ARCADE, subSystemId, coinopsVersion)
                            .load("list/coinops/" + coinopsVersion + "/" + subSystemId + "_roms.ls", 0, merge)) {
                        LOGGER.info(coinopsVersion + " " + subSystemId + " rom list completed!");
                    }
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
        private String subSystemId;
        private String coinopsVersion;

        public RomSetLoad(String systemId, String subSystemId, String coinopsVersion) throws Exception {
            super(Game.class);
            this.systemId = systemId;
            this.subSystemId = subSystemId;
            this.coinopsVersion = coinopsVersion;
        }

        @Override
        protected List<Game> getMongoDocumentByList(String file, long fileSize) throws Exception {
            String gameId = StringUtils.substring(file, 0, StringUtils.lastIndexOf(file, "."));

            gameInfoStatic.originalName = gameId;
            GameUtil.pretifyName(gameInfoStatic);

            Game game = null;
            String querySystemId = GameSystem.MAME.contains(systemId) ? GameSystem.ARCADE : systemId;
            String versionId = null;
            if (gameInfoStatic.version == null) {
                gameId = GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0));
            } else {
                gameId = GeneralUtil.alphanumerify(IterableUtils.get(gameInfoStatic.names, 0));
                versionId = GeneralUtil.alphanumerify(gameInfoStatic.version);
            }

            game = MongoHelper.getHelper(Game.class).getDocumentById(Game.createId(querySystemId, gameId, versionId));
            if (game == null) {
                game = new Game(querySystemId, gameId, versionId, fileSize);
                game.addLabel(game.gameId);
            }

            game.coinopsVersions = SetUtil.addValueToSortedSet(game.coinopsVersions, coinopsVersion);
            if (!StringUtils.equals(subSystemId, systemId)) {
                game.coinopsGameInfo = new GameInfo(gameId, subSystemId);
            }

            return Arrays.asList(game);
        }
    }
}
