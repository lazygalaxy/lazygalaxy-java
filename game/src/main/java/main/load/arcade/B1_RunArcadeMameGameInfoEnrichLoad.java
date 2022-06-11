package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import java.util.*;

public class B1_RunArcadeMameGameInfoEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(B1_RunArcadeMameGameInfoEnrichLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            new MameGameInfoLoad().load("source/mame/mame240.xml", "machine", merge);
            LOGGER.info("latest mame enrich game completed!");
            new MameGameInfoLoad().load("source/mame/mame2010.xml", "game", merge);
            LOGGER.info("mame2010 enrich game completed!");
            new MameGameInfoLoad().load("source/mame/mame2003.xml", "game", merge);
            LOGGER.info("mame2003 enrich game completed!");
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }

    private static class MameGameInfoLoad extends XMLLoad<Game> {

        private Map<String, Game> mameGameByIdMap = new HashMap<String, Game>();
        private Map<String, List<Game>> mameGameByNameYearMap = new HashMap<String, List<Game>>();
        private Map<String, List<Game>> mameGameByNameMap = new HashMap<String, List<Game>>();

        public MameGameInfoLoad() throws Exception {
            super(Game.class);

            List<Game> games = GameUtil.getGames(false, false, null, null, Filters.in("systemId", GameSystem.MAME));
            for (Game game : games) {
                mameGameByIdMap.put(game.gameId, game);

                for (String name : game.labels) {
                    if (game.year != null) {
                        String mapKey = name + game.year;
                        List<Game> gameList = mameGameByNameYearMap.get(mapKey);
                        if (gameList == null) {
                            gameList = new ArrayList<Game>();
                            mameGameByNameYearMap.put(mapKey, gameList);
                        }
                        gameList.add(game);
                    }
                    String mapKey = name;
                    List<Game> gameList = mameGameByNameMap.get(mapKey);
                    if (gameList == null) {
                        gameList = new ArrayList<Game>();
                        mameGameByNameMap.put(mapKey, gameList);
                    }
                    gameList.add(game);
                }
            }
        }

        @Override
        protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
            Boolean isMechanical = XMLUtil.getAttributeAsBoolean(element, "ismechanical");
            String gameId = XMLUtil.getAttributeAsString(element, "name");
            Set<String> inputs = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");

            if ((isMechanical != null && isMechanical) || StringUtils.contains(gameId, "_") || (inputs != null && Constant.Control.isExcluded(inputs))) {
                return null;
            }

            gameId = GeneralUtil.alphanumerify(gameId);
            Game game = mameGameByIdMap.get(gameId);

            List<Game> returnGameList = new ArrayList<Game>();
            if (game == null) {

                gameInfoStatic.originalName = XMLUtil.getTagAsString(element, "description", 0);
                GameUtil.pretifyName(gameInfoStatic);

                String year = XMLUtil.getTagAsString(element, "year", 0);
                if (year != null && !StringUtils.equals(year, "1970")) {
                    year = StringUtils.left(year, 4);
                    if (StringUtils.contains(year, "?")) {
                        year = null;
                    }
                }

                if (gameInfoStatic.names != null) {
                    for (String name : gameInfoStatic.names) {

                        List<Game> mapGames = null;
                        if (year != null) {
                            mapGames = mameGameByNameYearMap.get(GeneralUtil.alphanumerify(name + year));
                        }

                        if (mapGames == null) {
                            mapGames = mameGameByNameMap.get(GeneralUtil.alphanumerify(name));
                        }

                        if (mapGames != null) {
                            for (Game mapGame : mapGames) {
                                if (mapGame.mameGameInfo == null
                                        || (mapGame.mameGameInfo.isGuess != null && mapGame.mameGameInfo.isGuess
                                        && gameId.compareTo(mapGame.mameGameInfo.gameId) < 0)) {
                                    returnGameList.addAll(process(mapGame, element, true, gameId));
                                }
                            }
                        }

                    }
                }

                // }
            } else {
                returnGameList.addAll(process(game, element, false, gameId));
            }

            return returnGameList;
        }

        private List<Game> process(Game game, Element element, Boolean isGuess, String gameId) throws Exception {
            List<Game> allGamesToReturn = new ArrayList<Game>();
            if (game.mameGameInfo != null) {
                return allGamesToReturn;
            }
            allGamesToReturn.add(game);

            String originalName = XMLUtil.getTagAsString(element, "description", 0);
            String year = XMLUtil.getTagAsString(element, "year", 0);
            String players = XMLUtil.getTagAttributeAsString(element, "input", "players", 0);
            String manufacturer = XMLUtil.getTagAsString(element, "manufacturer", 0);
            String rotate = XMLUtil.getTagAttributeAsString(element, "display", "rotate", 0);
            if (rotate == null) {
                rotate = XMLUtil.getTagAttributeAsString(element, "video", "orientation", 0);
            }
            Boolean isVertical = null;
            if (rotate != null && StringUtils.equalsAny(rotate, "90", "270", "vertical")) {
                isVertical = true;
            } else if (rotate != null && StringUtils.equalsAny(rotate, "0", "180", "horizontal")) {
                isVertical = false;
            }
            Set<String> inputs = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
            if (inputs == null || inputs.size() == 0) {
                String control = XMLUtil.getTagAttributeAsString(element, "input", "control", 0);
                if (StringUtils.endsWith(control, "way")) {
                    SetUtil.addValueToTreeSet(inputs, StringUtils.substring(control, 0, control.length() - 4));
                }
            }
            String ways = XMLUtil.getTagAttributeAsString(element, "control", "ways", 0);
            if (ways == null) {
                ways = XMLUtil.getTagAttributeAsString(element, "input", "control", 0);
                if (StringUtils.endsWith(ways, "way")) {
                    ways = GeneralUtil.numerify(ways);
                } else {
                    ways = null;
                }
            }
            Integer buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);
            if (buttons == null) {
                buttons = XMLUtil.getTagAttributeAsInteger(element, "input", "buttons", 0);
            }
            String status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
            String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");

            gameInfoStatic.originalName = manufacturer;
            GameUtil.pretifyName(gameInfoStatic);
            List<String> manufacturers = new ArrayList<String>();
            for (String name : gameInfoStatic.names) {
                manufacturers.add(name);
            }
            manufacturers.add(gameInfoStatic.version);

            game.mameGameInfo = new GameInfo(gameId, originalName, year, players, manufacturers, isVertical, inputs,
                    ways, buttons, status, isGuess);
            GameUtil.pretifyName(game.mameGameInfo);

            game.addLabel(gameId);
            for (String name : game.mameGameInfo.names) {
                game.addLabel(name);
                if (game.mameGameInfo.version != null) {
                    game.addLabel(name + game.mameGameInfo.version);
                }
            }

            // we only derive family games
            if (!StringUtils.isBlank(cloneOf)) {
                String cloneOfGameId = GeneralUtil.alphanumerify(cloneOf);
                List<Game> parentGames = GameUtil.getGames(false, false, cloneOf, null,
                        Filters.eq("gameId", cloneOfGameId), Filters.in("systemId", GameSystem.MAME));

                if (parentGames != null) {
                    for (Game parentGame : parentGames) {
                        if (!SetUtil.contains(parentGame.family, game.gameId)) {
                            parentGame.family = SetUtil.addValueToTreeSet(parentGame.family, parentGame.gameId);
                            parentGame.family = SetUtil.addValueToTreeSet(parentGame.family, game.gameId);

                            game.family = SetUtil.addValueToTreeSet(game.family, parentGame.gameId);
                            game.family = SetUtil.addValueToTreeSet(game.family, game.gameId);

                            allGamesToReturn.add(parentGame);
                        }
                    }
                }
            }

            return allGamesToReturn;
        }
    }
}
