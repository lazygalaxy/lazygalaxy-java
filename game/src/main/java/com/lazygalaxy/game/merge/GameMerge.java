package com.lazygalaxy.game.merge;

import org.apache.commons.lang3.StringUtils;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {
		super.apply(newDocument, storedDocument);

		if (storedDocument.hasRomSets()) {
			newDocument.addRomSet(storedDocument.romSets);
		}

		if (storedDocument.hasGenres()) {
			newDocument.addGenre(storedDocument.genres);
		}

		if (StringUtils.length(storedDocument.description) > StringUtils.length(newDocument.description)) {
			newDocument.description = storedDocument.description;
		}
	}
}
