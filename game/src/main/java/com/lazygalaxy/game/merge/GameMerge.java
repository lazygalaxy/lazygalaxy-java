package com.lazygalaxy.game.merge;

import java.util.TreeSet;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {
		super.apply(newDocument, storedDocument);

		if (newDocument.genres != null && storedDocument.genres != null) {
			newDocument.genres.addAll(storedDocument.genres);
		} else if (newDocument.genres == null && storedDocument.genres != null) {
			newDocument.genres = new TreeSet<String>(storedDocument.genres);
		}
	}
}
