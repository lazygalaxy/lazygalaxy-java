package main.check;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GenreInfo;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RunCoinOpsGenreChecks {
    private static final Logger LOGGER = LogManager.getLogger(RunCoinOpsGenreChecks.class);

    public static void main(String[] args) throws Exception {
        try {
            Merge<Game> merge = new FieldMerge<Game>();

            new CoinOpsGenreCheck(
                    new GenreInfo(Constant.Genre.RACING, null, null),
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.DRIVING, null)
            ).load("check/coinops/genre/racing.txt");
            LOGGER.info("genre check completed!");

        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }


    private static class CoinOpsGenreCheck extends TextFileLoad<Game> {
        private GenreInfo[] genreInfos;


        public CoinOpsGenreCheck(GenreInfo... genreInfos) throws Exception {
            super(Game.class);
            this.genreInfos = genreInfos;
        }

        @Override
        protected void doAfterLoad() {
            LOGGER.info("genre expected: " + genreInfos);
        }

        @Override
        protected List<Game> getMongoDocument(String romId) throws Exception {

            List<Game> games = GameUtil.getGames(false, false, null, null,
                    Filters.in("labels", GeneralUtil.alphanumerify(romId)));

            if (games != null) {
                for (Game game : games) {
                    boolean found = false;
                    for (GenreInfo genreInfo : genreInfos) {
                        if (StringUtils.equals(game.genre, genreInfo.main)) {
                            if (genreInfo.sub == null || StringUtils.equals(game.subGenre, genreInfo.sub)) {
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        LOGGER.error(game.gameId + ": " + game.genre + " " + game.subGenre);
                    }
                }
            }

            return null;
        }
    }
}
