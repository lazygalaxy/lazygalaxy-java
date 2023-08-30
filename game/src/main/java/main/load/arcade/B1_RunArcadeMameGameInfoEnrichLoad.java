package main.load.arcade;

import com.lazygalaxy.common.util.SetUtil;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Add the mame information (from various mame versions) to all games
 */
public class B1_RunArcadeMameGameInfoEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(B1_RunArcadeMameGameInfoEnrichLoad.class);
    private static final GameInfo gameInfoStatic = new GameInfo();

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            load("mameXXXX", "machine", merge);
            load("mame2010", "game", merge);
            load("mame2003", "game", merge);
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }

    private static void load(String emulator, String gameTagName, GameMerge merge) throws Exception {
        new MameGameInfoLoad(emulator).load("source/mame/" + emulator + ".xml", gameTagName, merge);
        LOGGER.info(emulator + " enrich game completed!");
    }

    private static class MameGameInfoLoad extends XMLLoad<Game> {

        private final Map<String, Game> mameGameByIdMap = new HashMap<String, Game>();
        private final Map<String, List<Game>> mameGameByNameYearMap = new HashMap<String, List<Game>>();

        private final String emulatorVersion;

        public MameGameInfoLoad(String emulatorVersion) throws Exception {
            super(Game.class);

            this.emulatorVersion = emulatorVersion;

            List<Game> games = GameUtil.getGames(false, false, null, null, Filters.in("systemId", GameSystem.MAME));
            // key games in various maps for faster access
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
                }
            }
        }

        @Override
        protected List<Game> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
            String gameId = XMLUtil.getAttributeAsString(element, "name");

            gameId = GeneralUtil.alphanumerify(gameId);
            Game game = mameGameByIdMap.get(gameId);

            List<Game> returnGameList = new ArrayList<Game>();
            if (game == null) {
                gameInfoStatic.originalName = XMLUtil.getTagAsString(element, "description", 0);
                GameUtil.pretifyName(gameInfoStatic);

                if (gameInfoStatic.names != null) {
                    String year = XMLUtil.getTagAsString(element, "year", 0);
                    if (year != null && !StringUtils.equals(year, "1970")) {
                        year = StringUtils.left(year, 4);
                        if (StringUtils.contains(year, "?")) {
                            year = null;
                        }
                    }

                    for (String name : gameInfoStatic.names) {
                        List<Game> mapGames = null;
                        if (year != null) {
                            mapGames = mameGameByNameYearMap.get(GeneralUtil.alphanumerify(name + year));
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
            } else {
                returnGameList.addAll(process(game, element, false, gameId));
            }

            return returnGameList;
        }

        private List<Game> process(Game game, Element element, Boolean isGuess, String gameId) throws Exception {
            List<Game> allGamesToReturn = new ArrayList<Game>();
            allGamesToReturn.add(game);
            if (game.mameGameInfo != null && !StringUtils.equals(game.mameGameInfo.emulatorVersions.iterator().next(), emulatorVersion)) {
                game.mameGameInfo.emulatorVersions = SetUtil.addValueToSortedSet(game.mameGameInfo.emulatorVersions, emulatorVersion);
                return allGamesToReturn;
            }

            String originalName = XMLUtil.getTagAsString(element, "description", 0);
            String year = XMLUtil.getTagAsString(element, "year", 0);
            String players = XMLUtil.getTagAttributeAsString(element, "input", "players", 0);
            String manufacturer = XMLUtil.getTagAsString(element, "manufacturer", 0);
            if (manufacturer != null) {
                manufacturer = RegExUtils.replaceAll(manufacturer, "/", " / ");
            }
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

            Set<String> tempInputs = XMLUtil.getTagAttributeAsStringSet(element, "control", "type");
            if (tempInputs == null || tempInputs.size() == 0) {
                tempInputs = XMLUtil.getTagAttributeAsStringSet(element, "input", "control");
            }

            String ways = null;
            Set<String> inputs = null;
            if (tempInputs != null && tempInputs.size() > 0) {
                ways = XMLUtil.getTagAttributeAsString(element, "control", "ways", 0);
                inputs = new TreeSet<String>();
                for (String input : tempInputs) {
                    if (StringUtils.endsWith(input, "way")) {
                        inputs.add(StringUtils.substring(input, 0, input.length() - 4));
                        if (ways == null) {
                            ways = GeneralUtil.numerify(input);
                        }
                    } else {
                        inputs.add(input);
                    }
                }
            }

            Integer buttons = XMLUtil.getTagAttributeAsInteger(element, "control", "buttons", 0);
            if (buttons == null) {
                buttons = XMLUtil.getTagAttributeAsInteger(element, "input", "buttons", 0);
            }
            String status = XMLUtil.getTagAttributeAsString(element, "driver", "status", 0);
            String cloneOf = XMLUtil.getAttributeAsString(element, "cloneof");

            List<String> manufacturers = null;
            if (!StringUtils.isBlank(manufacturer)) {
                gameInfoStatic.originalName = manufacturer;
                GameUtil.pretifyName(gameInfoStatic);
                manufacturers = new ArrayList<String>();
                for (String name : gameInfoStatic.names) {
                    manufacturers.add(name);
                }
                manufacturers.add(gameInfoStatic.version);
            }

            String subSystemId = null;

            String sourceFile = XMLUtil.getAttributeAsString(element, "sourcefile");
            if (StringUtils.contains(sourceFile, "atomiswave")) {
                subSystemId = GameSystem.ATOMISWAVE;
            } else if (StringUtils.contains(sourceFile, "dlair")) {
                subSystemId = GameSystem.DAPHNE;
            } else if (StringUtils.contains(sourceFile, "model3")) {
                subSystemId = GameSystem.MODEL3;
            } else if (StringUtils.contains(sourceFile, "naomi")) {
                subSystemId = GameSystem.NAOMI;
            } else if (StringUtils.contains(sourceFile, "neogeo")) {
                subSystemId = GameSystem.NEOGEO;
            } else if (StringUtils.contains(sourceFile, "triforce")) {
                subSystemId = GameSystem.TRIFORCE;
            }

            game.mameGameInfo = new GameInfo(gameId, subSystemId, originalName, year, players, manufacturers, game.mameGameInfo != null ? SetUtil.addValueToSortedSet(game.mameGameInfo.emulatorVersions, emulatorVersion) : SetUtil.addValueToSortedSet(null, emulatorVersion), isVertical, inputs,
                    ways, buttons, status, isGuess);
            GameUtil.pretifyName(game.mameGameInfo);

            game.addLabel(gameId);
            for (String name : game.mameGameInfo.names) {
                game.addLabel(name);
                if (game.mameGameInfo.version != null) {
                    game.addLabel(name + game.mameGameInfo.version);
                }
            }


            if (!StringUtils.isBlank(cloneOf)) {
                // if this game is a clone of another game, we need to set all the family specific information
                String cloneOfGameId = GeneralUtil.alphanumerify(cloneOf);
                game.family = SetUtil.addValueToSortedSet(game.family, cloneOfGameId);

                List<Game> familyGames = GameUtil.getGames(false, false, null, null,
                        Filters.in("systemId", GameSystem.MAME), Filters.in("family", game.family.remove(game.gameId)));
                if (familyGames != null) {
                    for (Game familyGame : familyGames) {
                        familyGame.family = SetUtil.addValueToSortedSet(familyGame.family, game.gameId);
                        game.family = SetUtil.addValueToSortedSet(game.family, familyGame.gameId);
                        allGamesToReturn.add(familyGame);
                    }
                }
            }
            game.family = SetUtil.addValueToSortedSet(game.family, game.gameId);
            return allGamesToReturn;
        }
    }
}
