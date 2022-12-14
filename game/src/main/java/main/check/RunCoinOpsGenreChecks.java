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
                    //These are real Driving games (No mazes, shooting or platforms)
                    new GenreInfo(Constant.Genre.DRIVING, null, null, null, null),
                    //Maze driving games
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.DRIVING, null, null, null),
                    //Shooter driving games
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.DRIVING, null, null, null),
                    //There is some element of driving
                    new GenreInfo(Constant.Genre.SHOOTER, null, Constant.SubGenre.DRIVING, null, null),
                    new GenreInfo(Constant.Genre.PLATFORM, null, Constant.SubGenre.DRIVING, null, null)

            ).load("check/coinops/genre/driving.txt");

            new CoinOpsGenreCheck(
                    //Fighter BeatEmUps
                    new GenreInfo(Constant.Genre.FIGHTER, Constant.SubGenre.BEATEMUP, null, null, null),
                    //Versus Coop games are somewhere between Versus and BeatEmUps
                    new GenreInfo(Constant.Genre.FIGHTER, Constant.SubGenre.VERSUSCOOP, null, null, null),
                    //Platform games that have some fighting
                    new GenreInfo(Constant.Genre.PLATFORM, Constant.SubGenre.FIGHTER, null, null, null),
                    //Maze games that have some fighting
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.FIGHTER, null, null, null),
                    //There is some element of fighting
                    new GenreInfo(Constant.Genre.SHOOTER, null, Constant.SubGenre.FIGHTER, null, null),
                    new GenreInfo(Constant.Genre.PLATFORM, null, Constant.SubGenre.FIGHTER, null, null)


            ).load("check/coinops/genre/fighter_beatemup.txt");

            new CoinOpsGenreCheck(
                    //Fighter Versus games
                    new GenreInfo(Constant.Genre.FIGHTER, Constant.SubGenre.VERSUS, null, null, null),
                    //Versus Coop games are somewhere between Versus and BeatEmUps
                    new GenreInfo(Constant.Genre.FIGHTER, Constant.SubGenre.VERSUSCOOP, null, null, null),
                    //Sports games that are in nature Versus games
                    new GenreInfo(Constant.Genre.SPORTS, null, Constant.SubGenre.VERSUS, null, null)
            ).load("check/coinops/genre/fighter_versus.txt");

            new CoinOpsGenreCheck(
                    new GenreInfo(Constant.Genre.PUZZLE, null, null, null, null),
                    new GenreInfo(null, null, Constant.Genre.PUZZLE, null, null),
                    new GenreInfo(null, Constant.SubGenre.BREAKOUT, null, null, null),
                    new GenreInfo(null, Constant.SubGenre.MAHJONG, null, null, null),
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.BALLGUIDE, null, null, null),
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.COLLECT, null, null, null),
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.OUTLINE, null, null, null)
            ).load("check/coinops/genre/puzzle.txt");

            new CoinOpsGenreCheck(
                    //Gunner indicates that there is a Gun (LightGun or Stick) used by the game
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.GUNNER, null, null, null),
                    //Other games simply have a joystick as an input used for the gun, but is ultimate a Gunner game
                    new GenreInfo(Constant.Genre.SHOOTER, null, Constant.SubGenre.GUNNER, null, null)
            ).load("check/coinops/genre/shooter_gunner.txt");

            new CoinOpsGenreCheck(
                    //Actual Run and Gun games
                    new GenreInfo(Constant.Genre.PLATFORM, Constant.SubGenre.SHOOTER, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.WALKING, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.Values.OTHER, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.FIELD, null, null, null),
                    new GenreInfo(null, null, Constant.SubGenre.SHOOTER, null, null),
                    //Maze games that have some shooting
                    new GenreInfo(Constant.Genre.MAZE, Constant.SubGenre.SHOOTER, null, null, null),
                    //Clearly Gunner games, could be excluded
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.GUNNER, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, null, Constant.SubGenre.GUNNER, null, null),
                    //not good
                    new GenreInfo(Constant.Genre.PLATFORM, Constant.SubGenre.FIGHTER, null, null, null),
                    new GenreInfo(Constant.Genre.FIGHTER, Constant.SubGenre.BEATEMUP, null, null, null)

            ).load("check/coinops/genre/shooter_runngun.txt");

            new CoinOpsGenreCheck(
                    //Real Sports games
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.DRIVING, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.FLYING, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.FIELD, null, null, null),
                    new GenreInfo(Constant.Genre.SHOOTER, Constant.SubGenre.GALLERY, null, null, null)

            ).load("check/coinops/genre/shooter_shootemup.txt");

            new CoinOpsGenreCheck(
                    //Real Sports games
                    new GenreInfo(Constant.Genre.SPORTS, null, null, null, null)
            ).load("check/coinops/genre/sports.txt");
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
            for (GenreInfo genreInfo : genreInfos) {
                LOGGER.info("genre expected: " + genreInfo);
            }
        }

        @Override
        protected List<Game> getMongoDocument(String romId) throws Exception {
            if (!StringUtils.startsWith(romId, "//")) {
                List<Game> games = GameUtil.getGames(false, false, romId, null,
                        Filters.exists("coinopsVersions"), Filters.in("family", GeneralUtil.alphanumerify(romId)));

                if (games != null) {
                    for (Game game : games) {
                        boolean found = false;
                        for (GenreInfo genreInfo : genreInfos) {
                            if (genreInfo.main == null || StringUtils.equals(game.genre, genreInfo.main)) {
                                if (genreInfo.sub == null || StringUtils.equals(game.subGenre, genreInfo.sub)) {
                                    if (genreInfo.sub2 == null || StringUtils.equals(game.sub2Genre, genreInfo.sub2)) {
                                        found = true;
                                    }
                                }
                            }
                        }
                        if (!found) {
                            LOGGER.error(game.gameId + ": " + game.genre + " " + game.subGenre + " " + game.sub2Genre + " " + game.camera);
                        }
                    }
                }
            }
            return null;
        }
    }
}
