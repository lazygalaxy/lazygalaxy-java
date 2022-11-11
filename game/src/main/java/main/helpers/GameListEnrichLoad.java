package main.helpers;

import com.google.common.collect.Lists;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GameListEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(GameListEnrichLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    protected static void load(String source, boolean mustCreate, boolean matchOnlyGameId, String... systemIds) throws Exception {
        GameMerge merge = new GameMerge();

        if (systemIds == null || systemIds.length == 0) {
            File startDir = Paths.get(ClassLoader.getSystemResource("source/" + source).toURI()).toFile();
            for (File systemFile : startDir.listFiles()) {
                if (systemFile.isDirectory()) {
                    File systemGameListFile = new File(systemFile, "gamelist.xml");
                    if (systemGameListFile.exists()) {
                        new GameListLoad(source, mustCreate, matchOnlyGameId, systemFile.getName()).load(systemGameListFile, "game", merge);
                        LOGGER.info(source + " " + systemFile.getName() + " enrich load completed!");
                    } else {
                        LOGGER.warn(source + " " + systemFile.getName() + " no gamelist.xml found!");
                    }
                }
            }
        } else {

            for (String systemId : systemIds) {
                URL url = ClassLoader.getSystemResource("source/" + source + "/" + systemId + "/gamelist.xml");
                if (url != null && url.toURI() != null && Paths.get(url.toURI()).toFile() != null) {
                    File systemGameListFile = Paths.get(url.toURI()).toFile();
                    if (systemGameListFile.exists()) {
                        new GameListLoad(source, mustCreate, matchOnlyGameId, systemId).load(systemGameListFile, "game", merge);
                        LOGGER.info(source + " " + systemId + " enrich load completed!");
                    } else {
                        LOGGER.warn(source + " " + systemId + " no gamelist.xml found!");
                    }
                } else {
                    LOGGER.debug("Nothing to load: " + systemId);
                }
            }
        }
        LOGGER.info("Finished loading: " + source);
    }

    private static class GameListLoad extends XMLLoad<Game> {
        private String source;
        private boolean mustCreate;
        private boolean matchOnlyGameId;
        private String systemId;


        private String defaultEmulator = null;
        private Map<String, String> emulatorMap = new HashMap<String, String>();

        public GameListLoad(String source, boolean mustCreate, boolean matchOnlyGameId, String systemId) throws Exception {
            super(Game.class);
            this.source = source;
            this.mustCreate = mustCreate;
            this.matchOnlyGameId = matchOnlyGameId;

            switch (systemId) {
                case Constant.GameEmulator.FBNEO:
                    this.defaultEmulator = "lr-fbneo";
                    this.systemId = Constant.GameSystem.ARCADE;
                    break;
                case Constant.GameEmulator.MAME2003:
                    this.defaultEmulator = "lr-mame2003";
                    this.systemId = Constant.GameSystem.ARCADE;
                    break;
                case Constant.GameEmulator.MAME2010:
                    this.defaultEmulator = "lr-mame2010";
                    this.systemId = Constant.GameSystem.ARCADE;
                    break;
                default:
                    URL fileURL = ClassLoader.getSystemResource("source/" + source + "/" + systemId + "/emulators.cfg");
                    if (fileURL != null) {
                        Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
                        lines.forEach(line -> {
                            if (line.startsWith("default")) {
                                defaultEmulator = RegExUtils.removeAll(StringUtils.split(line, "=")[1], "\"").trim();
                            }
                        });
                        lines.close();
                    }
                    fileURL = ClassLoader.getSystemResource("source/" + source + "/all/emulators.cfg");
                    if (fileURL != null) {
                        Stream<String> lines = Files.lines(Paths.get(fileURL.toURI()));
                        lines.forEach(line -> {
                            String[] lineTokens = StringUtils.split(line, "=");
                            if (lineTokens.length == 2) {
                                String systemIdTemp = StringUtils.substring(lineTokens[0], 0,
                                        StringUtils.indexOf(lineTokens[0], "_"));
                                String gameIdTemp = StringUtils.substring(lineTokens[0],
                                        StringUtils.indexOf(lineTokens[0], "_") + 1, lineTokens[0].length());

                                emulatorMap.put(
                                        GeneralUtil.alphanumerify(systemIdTemp) + "_" + GeneralUtil.alphanumerify(gameIdTemp),
                                        RegExUtils.removeAll(lineTokens[1], "\"").trim());
                            }
                        });
                        lines.close();
                    }

                    this.systemId = systemId;
                    break;
            }

        }

        @Override
        protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
            String path = XMLUtil.getTagAsString(element, "path", 0);
            if (StringUtils.endsWith(path, ".sh")) {
                return null;
            }
            String querySystemId = GameSystem.MAME.contains(systemId) ? GameSystem.ARCADE : systemId;

            //query by pure game id
            String gameId = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
            gameId = StringUtils.substring(gameId, StringUtils.indexOf(gameId, "/") + 1, gameId.length());
            gameId.toLowerCase().trim();
            List<Game> gameList = GameUtil.getGames(false, false, gameId, null, Filters.in("gameId", gameId),
                    Filters.eq("systemId", querySystemId));


            if (gameList == null && StringUtils.equals(systemId, GameSystem.SCUMMVM)) {
                //query by modified game id
                gameId = gameId.split("-")[0].trim();
                gameList = GameUtil.getGames(false, false, gameId, null, Filters.in("gameId", gameId),
                        Filters.eq("systemId", querySystemId));
            }

            String originalName = XMLUtil.getTagAsString(element, "name", 0);
//
//            if (gameList == null && originalName != null && !matchOnlyGameId) {
//                //query by game name
//                gameInfoStatic.originalName = originalName;
//                GameUtil.pretifyName(gameInfoStatic);
//
//                for (String name : gameInfoStatic.names) {
//                    String labelSearch = GeneralUtil.alphanumerify(name);
//
//                    gameList = GameUtil.getGames(false, false, gameId + " " + originalName, null, Filters.in("labels", labelSearch),
//                            Filters.eq("systemId", querySystemId));
//                    if (gameList != null) {
//                        break;
//                    }
//                }
//            }

            // for some systems we create the game
            if (gameList == null && mustCreate) {
                Game game = new Game(querySystemId, gameId, null, null);
                gameList = new ArrayList<Game>();
                gameList.add(game);
            } else if (gameList == null) {
                LOGGER.info("game not found: " + gameId + " " + originalName);
                return null;
            }

            String year = XMLUtil.getTagAsString(element, "releasedate", 0);
            String description = XMLUtil.getTagAsString(element, "desc", 0);
            String genre = XMLUtil.getTagAsString(element, "genre", 0);
            String subGenre = XMLUtil.getTagAsString(element, "subgenre", 0);
            String image = XMLUtil.getTagAsString(element, "image", 0);
            String video = XMLUtil.getTagAsString(element, "video", 0);
            String marquee = XMLUtil.getTagAsString(element, "marquee", 0);
            Double rating = XMLUtil.getTagAsDouble(element, "rating", 0);
            String players = XMLUtil.getTagAsString(element, "players", 0);
            String developer = XMLUtil.getTagAsString(element, "developer", 0);
            String publisher = XMLUtil.getTagAsString(element, "publisher", 0);
            String inputs = XMLUtil.getTagAsString(element, "inputs", 0);
            String ways = XMLUtil.getTagAsString(element, "ways", 0);
            Integer buttons = XMLUtil.getTagAsInteger(element, "buttons", 0);

            for (Game game : gameList) {
                String emulatorVersion = emulatorMap.containsKey(game.id) ? emulatorMap.get(game.id) : defaultEmulator;
                Game.class.getField(source + "GameInfo").set(game,
                        new GameInfo(game.gameId, systemId, path, originalName, year, description, genre, subGenre, image, video,
                                marquee, rating, players, Lists.newArrayList(developer, publisher),
                                SetUtil.addValueToTreeSet(null, emulatorVersion), inputs != null ? SetUtil.addValueToTreeSet(null, StringUtils.split(inputs, ",")) : null, ways, buttons));

                GameUtil.pretifyName((GameInfo) Game.class.getField(source + "GameInfo").get(game));

                game.addLabel(gameId);
                GameInfo gameInfo = (GameInfo) Game.class.getField(source + "GameInfo").get(game);

                if (gameInfo.names != null) {
                    for (String name : gameInfo.names) {
                        game.addLabel(name);
//                        if (gameInfo.version != null) {
//                            game.addLabel(name + gameInfo.version);
//                        }
                    }
                }

//                if (game.year == null) {
//                    game.year = gameInfo.year;
//                }
            }

            return gameList;
        }
    }
}
