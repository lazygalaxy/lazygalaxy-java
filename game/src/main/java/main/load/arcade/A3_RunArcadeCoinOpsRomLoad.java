package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.CoinOpsVersion;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class A3_RunArcadeCoinOpsRomLoad {

    private static final Logger LOGGER = LogManager.getLogger(A3_RunArcadeCoinOpsRomLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            for (String coinopsVersion : CoinOpsVersion.ALL) {
                if (!StringUtils.equals(coinopsVersion, CoinOpsVersion.OTHER)) {
                    new RomSetLoad(GameSystem.ARCADE, coinopsVersion)
                            .load("list/coinops/" + coinopsVersion + "/arcade_roms.ls", 0, merge);
                    LOGGER.info(coinopsVersion + " arcade rom list completed!");
                }
            }
            
            //Other
            new OtherLoad(GameSystem.ARCADE, CoinOpsVersion.OTHER)
                    .load("list/coinops/other.csv", 0, merge);

            LOGGER.info("Other rom list completed!");
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

            game.coinopsVersions = SetUtil.addValueToTreeSet(game.coinopsVersions, coinopsVersion);

            return Arrays.asList(game);
        }
    }

    private static class OtherLoad extends CSVLoad<Game> {
        private String systemId;
        private String coinopsVersion;

        public OtherLoad(String systemId, String coinopsVersion) throws Exception {
            super(Game.class);
            this.systemId = systemId;
            this.coinopsVersion = coinopsVersion;
        }

        @Override
        protected List<Game> getMongoDocument(String[] tokens) throws Exception {
            String gameId = tokens[0].trim().toLowerCase();

            Game game = game = MongoHelper.getHelper(Game.class).getDocumentById(Game.createId(systemId, gameId, null));
            if (game == null) {
                game = new Game(systemId, gameId, null, null);
                game.addLabel(game.gameId);
            }

            if (game.coinopsVersions == null || game.coinopsVersions.size() == 0) {
                game.coinopsVersions = SetUtil.addValueToTreeSet(game.coinopsVersions, coinopsVersion);
            }

            return Arrays.asList(game);
        }
    }
}
