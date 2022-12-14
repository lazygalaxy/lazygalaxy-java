package main.load.common;

import com.google.common.collect.Lists;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.domain.GenreInfo;
import com.lazygalaxy.game.merge.GameMerge;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class C1_RunCommonDeriveEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(C1_RunCommonDeriveEnrichLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            GameMerge merge = new GameMerge();

            new DeriveSourceEnrichLoad().load(merge, null);
            LOGGER.info("derive source enrich completed!");
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }

    private static Map<String, Set<Game>> getMapObject() throws Exception {
        Map<String, Set<Game>> mameGameByNameSetMap = new HashMap<String, Set<Game>>();

        List<Game> games = GameUtil.getGames(false, false, null, null, Filters.in("systemId", GameSystem.MAME));
        for (Game game : games) {
            for (String name : game.labels) {
                Set<Game> gameList = mameGameByNameSetMap.get(name);
                if (gameList == null) {
                    gameList = new HashSet<Game>();
                    mameGameByNameSetMap.put(name, gameList);
                }
                gameList.add(game);
            }
            if (game.family != null) {
                for (String name : game.family) {
                    Set<Game> gamSet = mameGameByNameSetMap.get(name);
                    if (gamSet == null) {
                        gamSet = new HashSet<Game>();
                        mameGameByNameSetMap.put(name, gamSet);
                    }
                    gamSet.add(game);
                }
            }
        }

        return mameGameByNameSetMap;
    }

    private static class DeriveSourceEnrichLoad extends MongoLoad<Game, Game> {

        public DeriveSourceEnrichLoad() throws Exception {
            super(Game.class, Game.class);

        }

        @Override
        protected List<Game> getMongoDocument(Game game) throws Exception {
            game.subSystemId = null;
            setField(game, "systemId");
            setField(game, "names");
            setField(game, "year");
            setField(game, "players");
            setField(game, "description");
            setField(game, "genre");
            setField(game, "version");
            game.developer = null;
            game.publisher = null;
            setField(game, "manufacturers");
            setField(game, "isVertical");
            setField(game, "inputs");
            setField(game, "buttons");
            setField(game, "ways");

            return Arrays.asList(game);
        }

        private void setField(Game game, String field) throws Exception {
            for (String gameSource : GameSource.ALL) {
                GameInfo gameInfoObject = (GameInfo) Game.class.getField(gameSource + "GameInfo").get(game);
                if (gameInfoObject != null) {
                    Object fieldObject = getField(gameInfoObject, field);
                    if (fieldObject != null) {
                        if (StringUtils.equals(field, "names")) {
                            game.name = Lists.newArrayList((List<String>) fieldObject).get(0);
                            return;
                        } else if (StringUtils.equals(field, "manufacturers")) {
                            List<String> manufacturers = (List<String>) fieldObject;

                            if (game.developer == null) {
                                game.developer = manufacturers.get(0);
                                game.publisher = manufacturers.get(0);
                                game.manufacturer = GameUtil.normalizeManufacturer(manufacturers.get(0), true);
                            }

                            for (String manufacturer : manufacturers) {
                                if (!StringUtils.equals(game.publisher, manufacturer)) {
                                    game.publisher = manufacturer;
                                    if (StringUtils.equals(game.manufacturer, "Other")) {
                                        game.manufacturer = GameUtil.normalizeManufacturer(manufacturer, true);
                                        if (!StringUtils.equals(game.manufacturer, "Other")) {
                                            return;
                                        }
                                    }
                                }
                            }
                            return;
                        } else if (StringUtils.equals(field, "systemId")) {
                            if (fieldObject != null && !StringUtils.equals(game.systemId, (String) fieldObject)) {
                                game.subSystemId = (String) fieldObject;
                                return;
                            }
                        } else if (StringUtils.equals(field, "genre")) {
                            String genre = (String) fieldObject;
                            String subGenre = (String) getField(gameInfoObject, "subGenre");
                            subGenre = !StringUtils.isBlank(subGenre) ? subGenre : Constant.Values.UNKNOWN;

                            String sub2Genre = (String) getField(gameInfoObject, "sub2Genre");
                            sub2Genre = !StringUtils.isBlank(sub2Genre) ? sub2Genre : Constant.Values.UNKNOWN;

                            GenreInfo genreInfo = new GenreInfo(genre, subGenre, sub2Genre, null, null);
                            GameUtil.normalizeGenres(genreInfo, game.name);

                            game.genre = genreInfo.main;
                            game.subGenre = genreInfo.sub;
                            game.sub2Genre = genreInfo.sub2;
                            game.camera = genreInfo.camera;
                            game.graphics = genreInfo.graphics;
                            return;
                        } else {
                            Game.class.getField(field).set(game, fieldObject);
                            return;
                        }
                    }
                }
            }
        }

        private Object getField(GameInfo gameInfo, String field) throws Exception {
            if (gameInfo != null) {
                Object fieldObject = GameInfo.class.getField(field).get(gameInfo);
                if (fieldObject != null) {
                    return fieldObject;
                }
            }
            return null;
        }
    }
}
