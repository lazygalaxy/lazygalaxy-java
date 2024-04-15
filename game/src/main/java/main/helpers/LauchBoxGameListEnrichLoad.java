package main.helpers;

import com.google.common.collect.Lists;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauchBoxGameListEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(LauchBoxGameListEnrichLoad.class);
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
                        new GameListLoad(source, mustCreate, matchOnlyGameId, systemId).load(systemGameListFile, "Game", merge);
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
            this.systemId = systemId;
        }

        @Override
        protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
            String path = XMLUtil.getTagAsString(element, "ApplicationPath", 0);
            String querySystemId = GameSystem.MAME.contains(systemId) ? GameSystem.ARCADE : systemId;

            //query by pure game id
            String gameId = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
            gameId = StringUtils.substring(gameId, StringUtils.lastIndexOf(gameId, "\\") + 1, gameId.length());
            gameId.toLowerCase().trim();
            List<Game> gameList = GameUtil.getGames(false, false, gameId, null, Filters.in("gameId", gameId),
                    Filters.eq("systemId", querySystemId));


            if (gameList == null && StringUtils.equals(systemId, GameSystem.SCUMMVM)) {
                //query by modified game id
                gameId = gameId.split("-")[0].trim();
                gameList = GameUtil.getGames(false, false, gameId, null, Filters.in("gameId", gameId),
                        Filters.eq("systemId", querySystemId));
            }

            String originalName = XMLUtil.getTagAsString(element, "Title", 0);

            // for some systems we create the game
            if (gameList == null && mustCreate) {
                Game game = new Game(querySystemId, gameId, null, null);
                gameList = new ArrayList<Game>();
                gameList.add(game);
            } else if (gameList == null) {
                LOGGER.info("game not found: " + gameId + " " + originalName);
                return null;
            }

            String year = XMLUtil.getTagAsString(element, "ReleaseDate", 0);
            String description = XMLUtil.getTagAsString(element, "Notes", 0);
            String genre = XMLUtil.getTagAsString(element, "Genre", 0);
            Double rating = XMLUtil.getTagAsDouble(element, "CommunityStarRating", 0);
            String players = XMLUtil.getTagAsString(element, "MaxPlayers", 0);
            String developer = XMLUtil.getTagAsString(element, "Developer", 0);
            String publisher = XMLUtil.getTagAsString(element, "Publisher", 0);

            for (Game game : gameList) {
                Game.class.getField(source + "GameInfo").set(game,
                        new GameInfo(game.gameId, systemId, path, originalName, year, description, genre, null, null, null,
                                null, rating, players, Lists.newArrayList(developer, publisher),
                                null, null, null, null));

                GameUtil.pretifyName((GameInfo) Game.class.getField(source + "GameInfo").get(game));

                game.addLabel(gameId);
                GameInfo gameInfo = (GameInfo) Game.class.getField(source + "GameInfo").get(game);

                if (gameInfo.names != null) {
                    for (String name : gameInfo.names) {
                        game.addLabel(name);
                    }
                }
                if (!StringUtils.isBlank(year) && StringUtils.isBlank(game.year)) {
                    game.year = gameInfo.year;
                }
            }

            return gameList;
        }
    }
}
