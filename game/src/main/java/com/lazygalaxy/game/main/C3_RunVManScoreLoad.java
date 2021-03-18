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
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtil;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;

public class C3_RunVManScoreLoad {

	private static final Logger LOGGER = LogManager.getLogger(C3_RunVManScoreLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			Merge<Scores> merge = new FieldMerge<Scores>();

			new VManExtensiveScoreLoad().load("xml/vman/retroarch_arcade_games.xml", "game", merge);
			new VManExtensiveScoreLoad().load("xml/vman/retroarch_atomiswave_games.xml", "game",
					new FieldMerge<Scores>(), "System");
			new VManExtensiveScoreLoad().load("xml/vman/retroarch_daphne_games.xml", "game", merge);
			new VManExtensiveScoreLoad().load("xml/vman/retroarch_naomi_games.xml", "game", merge);
			new VManExtensiveScoreLoad().load("xml/vman/retroarch_neogeo_games.xml", "game", merge);
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}

	private static class VManExtensiveScoreLoad extends XMLLoad<Scores> {

		public VManExtensiveScoreLoad() throws Exception {
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
			String name = GeneralUtil.alphanumerify(GameUtil.pretify(XMLUtil.getTagAsString(element, "name", 0)));
			String year = StringUtils.left(XMLUtil.getTagAsString(element, "releasedate", 0), 4);

			List<Game> games = null;

			if (!StringUtils.isBlank(name) && !StringUtils.isBlank(year)) {
				games = GameUtil.getGames(false, true, name + " " + year,
						Filters.and(Filters.in("labels", name), Filters.eq("year", year)));
			}

			if (games == null && !StringUtils.isBlank(rom)) {
				if (StringUtils.isBlank(alternativeRom)) {
					alternativeRom = rom;
				}
				games = GameUtil.getGames(false, true, rom + " " + alternativeRom,
						Filters.or(Filters.eq("rom", rom), Filters.eq("rom", alternativeRom), Filters.in("clones", rom),
								Filters.in("clones", alternativeRom)));
			}

			Double rating = XMLUtil.getTagAsDouble(element, "rating", 0);
			if (rating != null && rating == 0.0) {
				rating = null;
			}

			if (games != null && rating != null) {
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
