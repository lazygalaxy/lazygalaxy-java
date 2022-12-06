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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Add the category information from Arcade Italia on the entire family of a specific game
 */
public class B2_RunArcadeItaliaCategoryEnrichLoad {
    private static final Logger LOGGER = LogManager.getLogger(B2_RunArcadeItaliaCategoryEnrichLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            Merge<Game> merge = new FieldMerge<Game>();

            new ArcadeItaliaCategryLoad().load("source/arcadeitalia/category.ini", 8, merge);
            LOGGER.info("category load completed!");

        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }


    private static class ArcadeItaliaCategryLoad extends TextFileLoad<Game> {
        private GenreInfo lastGenre = null;

        public ArcadeItaliaCategryLoad() throws Exception {
            super(Game.class);
            lastGenre = null;
        }

        @Override
        protected List<Game> getMongoDocument(String romId) throws Exception {

            if (StringUtils.startsWith(romId, "[") && StringUtils.endsWith(romId, "]")) {
                //if the row is a new genre, keep track of teh last genre
                lastGenre = getGenre(romId);
            } else if (lastGenre != null) {
                // if the row is a game, get all the entire family
                List<Game> games = GameUtil.getGames(false, false, null, null,
                        Filters.in("systemId", GameSystem.MAME), Filters.in("family", romId));

                if (games != null) {
                    // if we actually have some games, we update the genre info
                    for (Game game : games) {
                        if (game.arcadeitaliaGameInfo == null) {
                            game.arcadeitaliaGameInfo = new GameInfo(romId, null);
                        }

                        game.arcadeitaliaGameInfo.genre = lastGenre.main;
                        game.arcadeitaliaGameInfo.subGenre = lastGenre.sub;
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
            return new GenreInfo(mainGenre, subGenre, null);
        }
    }
}
