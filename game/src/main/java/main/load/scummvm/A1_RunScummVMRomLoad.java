package main.load.scummvm;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.LinuxListLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class A1_RunScummVMRomLoad {

    private static final Logger LOGGER = LogManager.getLogger(A1_RunScummVMRomLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            new ScummVMRomLoad().load("scummvm/scummvm_working_roms.ls", 0, merge);
            LOGGER.info("scummvm working rom list completed!");

            new ScummVMRomLoad().load("scummvm/scummvm_other_roms.ls", 0, merge);
            LOGGER.info("scummvm other rom list completed!");

        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }

    private static class ScummVMRomLoad extends LinuxListLoad<Game> {
        private Map<String, GameInfo> gameInfoMap = new TreeMap<String, GameInfo>();

        public ScummVMRomLoad() throws Exception {
            super(Game.class);

            Path filePath = Paths.get(ClassLoader.getSystemResource("scummvm/scummvm_id_map.txt").toURI());
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] tokens = StringUtils.split(line, "\t");
                if (tokens.length == 3) {
                    GameInfo gameInfo = new GameInfo();
                    gameInfo.gameId = tokens[0].toLowerCase().trim();
                    gameInfo.originalName = tokens[2].trim();
                    GameUtil.pretifyName(gameInfo);

                    for (String name : gameInfo.names) {
                        String key = GeneralUtil.alphanumerify(name);
                        if (!StringUtils.equalsAny(key, "livingbooks", "crazynickspicks", "magictales",
                                "lostfilesofsherlockholmesthe", "lostfilesofsherlockholmes", "kingsquest1", "spacequest1",
                                "policequest1", "leisuresuitlarry1", "questforglory1", "laurabow")) {
                            if (!gameInfoMap.containsKey(key)) {
                                gameInfoMap.put(key, gameInfo);
                            } else {
                                LOGGER.error(key + " -> duplicate found");
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected List<Game> getMongoDocumentByList(String file, long fileSize) throws Exception {
            String originalName = file;
            if (StringUtils.contains(file, ".")) {
                originalName = StringUtils.substring(file, 0, StringUtils.lastIndexOf(file, "."));
            }
            GameInfo newGameInfo = new GameInfo(null, file, originalName);
            GameUtil.pretifyName(newGameInfo);

            GameInfo referenceGameInfo = null;

            for (String name : newGameInfo.names) {
                referenceGameInfo = gameInfoMap
                        .get(GeneralUtil.alphanumerify(name + includeVersion(newGameInfo.version)));
                if (referenceGameInfo != null) {
                    break;
                }
            }

            if (referenceGameInfo != null) {
                String version = newGameInfo.version;
                newGameInfo.originalName = referenceGameInfo.originalName;
                GameUtil.pretifyName(newGameInfo);
                newGameInfo.version = version;
                newGameInfo.gameId = referenceGameInfo.gameId;

                Game game = MongoHelper.getHelper(Game.class)
                        .getDocumentById(Game.createId(Constant.GameSystem.SCUMMVM, newGameInfo.gameId, newGameInfo.version));
                if (game == null) {
                    game = new Game(Constant.GameSystem.SCUMMVM, newGameInfo.gameId, newGameInfo.version, fileSize);
                }

                game.scummvmGameInfo = newGameInfo;
//                game.name = game.scummvmGameInfo.names.get(0);

                game.addLabel(game.gameId);
                game.addLabel(referenceGameInfo.gameId);
                if (newGameInfo.names != null) {
                    for (String name : newGameInfo.names) {
                        game.addLabel(name);
//                        if (newGameInfo.version != null) {
//                            game.addLabel(name + newGameInfo.version);
//                        }
                    }
                }

                return Arrays.asList(game);

            }
            LOGGER.error(originalName + " -> not found");
            return null;
        }

        private String includeVersion(String version) {
            if (version != null && StringUtils.contains(version.toLowerCase(), "remake")) {
                return " [Remake]";
            }
            return "";
        }

    }
}
