package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import com.lazygalaxy.game.util.GameUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;


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
        private String lastGenre = null;
        private String lastSubGenre = null;

        public ArcadeItaliaCategryLoad() throws Exception {
            super(Game.class);
            lastGenre = null;
            lastSubGenre = null;
        }

        @Override
        protected List<Game> getMongoDocument(String romId) throws Exception {
            if (StringUtils.startsWith(romId, "[") && StringUtils.endsWith(romId, "]")) {
                String[] tokens = GeneralUtil.split(StringUtils.substring(romId, 1, romId.length() - 1), "/");
                lastGenre = tokens[0].trim();
                if (tokens.length >= 2) {
                    lastSubGenre = tokens[1].trim();
                } else {
                    lastSubGenre = Constant.Values.OTHER;
                }


            } else if (!StringUtils.isBlank(lastGenre)) {
                Pair<String, String> genreInfo = GameUtil.normalizeGenres(lastGenre, lastSubGenre, romId, null);
                String normalizedLastGenre = genreInfo.getLeft();
                String normalizedLastSubGenre = genreInfo.getRight();

                Game game = MongoHelper.getHelper(Game.class).getDocumentById(Game.createId(GameSystem.ARCADE, romId, null));

                if (game != null) {
                    if (game.arcadeitaliaGameInfo == null) {
                        game.arcadeitaliaGameInfo = new GameInfo(romId, null);
                    }

                    int posiGenreOld = Constant.Genre.ALL.indexOf(game.arcadeitaliaGameInfo.genre);
                    int posiGenreNew = Constant.Genre.ALL.indexOf(normalizedLastGenre);

                    int posiSubGenreOld = Constant.Genre.ALL.indexOf(game.arcadeitaliaGameInfo.subGenre);
                    int posiSubGenreNew = Constant.Genre.ALL.indexOf(normalizedLastSubGenre);

                    if (game.arcadeitaliaGameInfo.genre == null || posiGenreNew > posiGenreOld || ((posiGenreNew == posiGenreOld) && posiSubGenreNew > posiSubGenreOld)) {
                        game.arcadeitaliaGameInfo.genre = normalizedLastGenre;
                        game.arcadeitaliaGameInfo.subGenre = normalizedLastSubGenre;
                    }

                    return Arrays.asList(game);
                }
            }

            return null;
        }
    }
}
