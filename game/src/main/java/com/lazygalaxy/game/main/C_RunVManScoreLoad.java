package com.lazygalaxy.game.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C_RunVManScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C_RunVManScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new VManScoreLoad().load("xml/vman/retroarch_atomiswave_games.xml", "game", new FieldMerge<Scores>(),
					"System");
			new VManScoreLoad().load("xml/vman/retroarch_daphne_games.xml", "game", new FieldMerge<Scores>(), "System");
			new VManScoreLoad().load("xml/vman/retroarch_arcade_games.xml", "game", new FieldMerge<Scores>(), "System");
			new VManScoreLoad().load("xml/vman/retroarch_naomi_games.xml", "game", new FieldMerge<Scores>(), "System");
			new VManScoreLoad().load("xml/vman/retroarch_neogeo_games.xml", "game", new FieldMerge<Scores>(), "System");
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class VManScoreLoad extends XMLLoad<Scores> {

		public VManScoreLoad() throws Exception {
			super(Scores.class);
		}

		@Override
		protected List<Scores> getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String path = XMLUtil.getTagAsString(element, "path", 0);
			String rom = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
			rom = StringUtils.substring(rom, StringUtils.lastIndexOf(rom, "/") + 1, rom.length());

			String image = XMLUtil.getTagAsString(element, "image", 0);
			String alternativeRom = null;
			if (image != null) {
				alternativeRom = StringUtils.substring(image, 0, StringUtils.lastIndexOf(image, "."));
				alternativeRom = StringUtils.substring(alternativeRom, StringUtils.lastIndexOf(alternativeRom, "/") + 1,
						alternativeRom.length());
			}
			Double rating = XMLUtil.getTagAsDouble(element, "rating", 0);
			if (rating == null) {
				rating = 0.0;
			}

			List<Game> games = GameUtil.getGames(true, true, rom + " " + alternativeRom,
					Filters.or(Filters.eq("rom", rom), Filters.eq("rom", alternativeRom), Filters.in("clones", rom),
							Filters.in("clones", alternativeRom)));
			if (games != null) {
				List<Scores> scoresList = new ArrayList<Scores>();
				for (Game game : games) {
					Scores scores = new Scores(game.id);
					scores.vman = (int) Math.round(rating * 100.0);
					scoresList.add(scores);
				}

				return scoresList;
			}
			return null;
		}
	}

}
