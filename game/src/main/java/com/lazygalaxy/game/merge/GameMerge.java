package com.lazygalaxy.game.merge;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {

		super.apply(newDocument, storedDocument);

		newDocument.genre.addAll(storedDocument.genre);
		newDocument.collections.addAll(storedDocument.collections);
	}
}
