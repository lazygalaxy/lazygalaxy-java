package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.load.MongoLoad;
import com.lazygalaxy.engine.load.TextFileLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C1_RunTopScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C1_RunTopScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Scores> merge = new FieldMerge<Scores>();

			new WatchMojoScoreLoad().load("score/score_arcade_watchmojo.csv", 0, merge);
			LOGGER.info("watch mojo score load completed!");

			new AntopisaScoreLoad().load("score/score_arcade_antopisa.ini", 8, merge);
			LOGGER.info("antopisa score load completed!");

			new SourceScoreLoad().load(merge);
			LOGGER.info("source score load completed!");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class WatchMojoScoreLoad extends CSVLoad<Scores> {

		public WatchMojoScoreLoad() throws Exception {
			super(Scores.class);
		}

		@Override
		protected List<Scores> getMongoDocument(String[] tokens) throws Exception {
			Integer rating = Integer.parseInt(tokens[0]);
			String name = GeneralUtil.alphanumerify(tokens[1]);
			String year = tokens[2];

			List<Game> games = GameUtil.getGames(
					true, true, name + " " + year, Filters.in("labels", name), Filters.in("systemId", GameSystem.ARCADE,
							GameSystem.ATOMISWAVE, GameSystem.DAPHNE, GameSystem.NAOMI, GameSystem.NEOGEO),
					Filters.eq("year", year));

			if (games != null) {
				List<Scores> scoresList = new ArrayList<Scores>();

				for (Game game : games) {
					Scores scores = new Scores(game.id);
					// (max*2) - 1
					scores.watchMojo = (int) Math.round(((46 - rating) / 45.0) * 100.0);
					scoresList.add(scores);
				}

				return scoresList;
			}
			return null;
		}
	}

	private static class AntopisaScoreLoad extends TextFileLoad<Scores> {
		private Integer lastScore = null;

		public AntopisaScoreLoad() throws Exception {
			super(Scores.class);
			lastScore = null;
		}

		@Override
		protected List<Scores> getMongoDocument(String romId) throws Exception {
			String[] tokens = GeneralUtil.split(romId, " ");
			if (tokens.length >= 4) {
				lastScore = Integer.parseInt(tokens[2]);
			} else if (lastScore != null) {
				List<Game> games = GameUtil.getGames(false, true, romId,
						Filters.or(Filters.eq("romId", romId), Filters.eq("cloneOfRomId", romId)),
						Filters.in("systemId", GameSystem.ARCADE, GameSystem.ATOMISWAVE, GameSystem.DAPHNE,
								GameSystem.NAOMI, GameSystem.NEOGEO));

				if (games != null) {
					List<Scores> scoresList = new ArrayList<Scores>();
					for (Game game : games) {
						Scores scores = new Scores(game.id);
						scores.antopisa = lastScore;
						scoresList.add(scores);
					}

					return scoresList;
				}
			}

			return null;
		}
	}

	private static class SourceScoreLoad extends MongoLoad<Game, Scores> {

		public SourceScoreLoad() throws Exception {
			super(Game.class, Scores.class);
		}

		@Override
		protected List<Scores> getMongoDocument(Game game) throws Exception {

			Scores scores = new Scores(game.id);
			if (game.rickdangerous_ultimateGameInfo != null && game.rickdangerous_ultimateGameInfo.rating != null) {
				scores.rickDangerous = (int) Math.round(game.rickdangerous_ultimateGameInfo.rating * 100.0);
			}
			if (game.vman_blissGameInfo != null && game.vman_blissGameInfo.rating != null) {
				scores.vman = (int) Math.round(game.vman_blissGameInfo.rating * 100.0);
			}
			if (game.wolfanoz_12kGameInfo != null && game.wolfanoz_12kGameInfo.rating != null) {
				scores.wolfanoz = (int) Math.round(game.wolfanoz_12kGameInfo.rating * 100.0);
			}

			return Arrays.asList(scores);
		}

	}
}
