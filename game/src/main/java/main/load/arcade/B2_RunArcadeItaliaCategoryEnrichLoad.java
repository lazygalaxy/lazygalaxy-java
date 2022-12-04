package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.domain.GenreInfo;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class B2_RunArcadeItaliaCategoryEnrichLoad {

    private static final Logger LOGGER = LogManager.getLogger(B2_RunArcadeItaliaCategoryEnrichLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            Merge<Game> merge = new FieldMerge<Game>();

            new ArcadeItaliaCategryLoad().load("source/arcadeitalia/Category.ini", 6, merge);
            LOGGER.info("category load completed!");

        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }


    private static class ArcadeItaliaCategryLoad extends TextFileLoad<Game> {
        private GenreInfo lastGenre = null;

        private final Map<String, List<GenreInfo>> genreListMap = new HashMap<String, List<GenreInfo>>();

        public ArcadeItaliaCategryLoad() throws Exception {
            super(Game.class);
            lastGenre = null;
        }

        @Override
        public boolean load(String file, long skipLines, Merge<Game> merge) throws Exception {
            URL url = ClassLoader.getSystemResource(file);
            lastGenre = null;
            BufferedReader reader = Files.newBufferedReader(Paths.get(url.toURI()));

            while (reader.ready()) {
                String line = reader.readLine();
                if (StringUtils.startsWith(line, "[") && StringUtils.endsWith(line, "]")) {
                    lastGenre = getGenre(line);
                } else if (lastGenre != null) {
                    List<GenreInfo> genreList = genreListMap.get(line);
                    if (genreList == null) {
                        genreList = new TreeList<GenreInfo>();
                        genreListMap.put(line, genreList);
                    }
                    genreList.add(lastGenre);
                }
            }

            lastGenre = null;
            return super.load(file, skipLines, merge);
        }

        @Override
        protected List<Game> getMongoDocument(String romId) throws Exception {
            if (StringUtils.startsWith(romId, "[") && StringUtils.endsWith(romId, "]")) {
                lastGenre = getGenre(romId);
            } else if (lastGenre != null) {
                List<Game> games = GameUtil.getGames(false, false, null, null,
                        Filters.in("systemId", GameSystem.MAME), Filters.in("family", romId));

                if (games != null) {
                    GenreInfo normalizedGenre = GameUtil.normalizeGenres(lastGenre, romId, null);

                    for (Game game : games) {
                        if (game.arcadeitaliaGameInfo == null) {
                            game.arcadeitaliaGameInfo = new GameInfo(romId, null);
                        }

                        int posiGenreOld = Constant.Genre.ALL.indexOf(game.arcadeitaliaGameInfo.genre);
                        int posiGenreNew = Constant.Genre.ALL.indexOf(normalizedGenre.main);

                        int posiSubGenreOld = Constant.Genre.ALL.indexOf(game.arcadeitaliaGameInfo.subGenre);
                        int posiSubGenreNew = Constant.Genre.ALL.indexOf(normalizedGenre.sub);

                        if (game.arcadeitaliaGameInfo.genre == null || posiGenreNew > posiGenreOld || ((posiGenreNew == posiGenreOld) && posiSubGenreNew > posiSubGenreOld)) {
                            game.arcadeitaliaGameInfo.genre = normalizedGenre.main;
                            game.arcadeitaliaGameInfo.subGenre = normalizedGenre.sub;
                        }
                    }

                    return games;
                }
            }

            return null;
        }

        private GenreInfo getGenre(String line) {
            String mainGenre = null;
            String subGenre = null;
            String[] tokens = GeneralUtil.split(StringUtils.substring(line, 1, line.length() - 1), "/");
            mainGenre = tokens[0].trim();
            if (tokens.length >= 2) {
                subGenre = tokens[1].trim();
            } else {
                subGenre = Constant.Values.OTHER;
            }
            return new GenreInfo(mainGenre, subGenre);
        }
    }
}
