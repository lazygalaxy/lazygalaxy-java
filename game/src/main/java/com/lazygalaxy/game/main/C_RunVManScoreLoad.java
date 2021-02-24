package com.lazygalaxy.game.main;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.XMLLoad;
import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.engine.util.XMLUtils;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.Scores;

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
		protected Scores getMongoDocument(Element element, List<String> extraTagValues) throws Exception {
			String path = XMLUtils.handleString(element, "path");
			String gameId = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "."));
			String systemId = GeneralUtil.alphanumerify(extraTagValues.get(0));

			Double rating = XMLUtils.handleDouble(element, "rating");
			if (rating == null) {
				rating = 0.0;
			}
			String id = gameId + systemId;

			Game game = MongoHelper.getHelper(Game.class).getDocumentById(id);
			if (game == null) {
				throw new Exception("game not found: " + id);
			}

			Scores scores = new Scores(game.id);
			scores.vman = (int) Math.round(rating * 100.0);
			return scores;
		}
	}

}
