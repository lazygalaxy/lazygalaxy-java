package com.lazygalaxy.game.merge;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.SetUtil;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {
		super.apply(newDocument, storedDocument);

		newDocument.romSets = SetUtil.addValue(newDocument.romSets, storedDocument.romSets);
		newDocument.clones = SetUtil.addValue(newDocument.clones, storedDocument.clones);
		newDocument.collections = SetUtil.addValue(newDocument.collections, storedDocument.collections);
		newDocument.input = SetUtil.addValue(newDocument.input, storedDocument.input);
	}
}
