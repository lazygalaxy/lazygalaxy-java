package com.lazygalaxy.game.merge;

import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.Constant.Control;
import com.lazygalaxy.game.Constant.Genre;
import com.lazygalaxy.game.domain.Game;

public class GameMerge extends FieldMerge<Game> {
	private static final Logger LOGGER = LogManager.getLogger(GameMerge.class);

	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {

		super.apply(newDocument, storedDocument);

		newDocument.collections.addAll(storedDocument.collections);

		newDocument.genre.addAll(storedDocument.genre);
		if (newDocument.genre.contains(Genre.LIGHTGUN)) {
			if (newDocument.input == null) {
				newDocument.input = new TreeSet<String>();
			} else {
				newDocument.input.clear();
			}
			newDocument.input.add(Control.LIGHTGUN);
			newDocument.genreLabel = Genre.SHOOTER + " > " + Genre.LIGHTGUN;
		}

		if (newDocument.genre.contains(Genre.SPORT)) {
			newDocument.genre.remove(Genre.SPORT);
			if (newDocument.genre.size() == 1) {
				newDocument.genreLabel = Genre.SPORT + " > " + newDocument.genre.toArray()[0];
			} else {
				LOGGER.warn(newDocument.romId + " unexpected genre " + newDocument.genre);
			}
			newDocument.genre.add(Genre.SPORT);
		}

		if (newDocument.genre.contains(Genre.SHOOTEMUP)) {
			newDocument.genreLabel = Genre.SHOOTER + " > " + Genre.SHOOTEMUP;
			if (newDocument.genre.contains("horizontal")) {
				newDocument.isVeritcal = false;
			} else if (newDocument.genre.contains("vertical")) {
				newDocument.isVeritcal = true;
			}
		}
	}
}
