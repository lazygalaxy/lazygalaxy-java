package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;


public class B2_RunArcadeCategoryEnrichLoad {

    private static final Logger LOGGER = LogManager.getLogger(B2_RunArcadeCategoryEnrichLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            Merge<Game> merge = new FieldMerge<Game>();

            new ArcadeItaliaCategryLoad().load("category/Category.ini", 6, merge);
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
                    lastSubGenre = "none";
                }
            } else if (!StringUtils.isBlank(lastGenre) && !StringUtils.equals(lastGenre, "Xtra")) {
                Game game = MongoHelper.getHelper(Game.class).getDocumentById(Game.createId(Constant.GameSystem.ARCADE, romId, null));

                if (game != null) {
                    if (game.lazygalaxyGameInfo == null) {
                        game.lazygalaxyGameInfo = new GameInfo(romId, null);
                    }
                    if (!StringUtils.equalsAny(game.lazygalaxyGameInfo.genre, "Racing", "Puzzle", "Sports") ||
                            StringUtils.equalsAny(lastGenre, "Racing", "Puzzle", "Sports")) {
                        game.lazygalaxyGameInfo.genre = lastGenre;

                        if (StringUtils.equals(lastGenre, "Sports") && StringUtils.equals(lastSubGenre, "1")) {
                            game.lazygalaxyGameInfo.subGenre = "Track & Field";
                        } else if (StringUtils.equals(lastGenre, "Sports") && StringUtils.equals(lastSubGenre, "2")) {
                            game.lazygalaxyGameInfo.subGenre = "none";
                        } else {
                            game.lazygalaxyGameInfo.subGenre = lastSubGenre;
                        }
                    }

                    return Arrays.asList(game);
                }
            }

            return null;
        }
    }
}
