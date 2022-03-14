package com.lazygalaxy.game.merge;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.SetUtil;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {
		super.apply(newDocument, storedDocument);

		if (newDocument.mameGameInfo != null && storedDocument.mameGameInfo != null) {
			newDocument.mameGameInfo.emulators = SetUtil.addValue(newDocument.mameGameInfo.emulators,
					storedDocument.mameGameInfo.emulators);
		}

		if (newDocument.rickdangerousGameInfo != null && storedDocument.rickdangerousGameInfo != null) {
			newDocument.rickdangerousGameInfo.emulators = SetUtil.addValue(newDocument.rickdangerousGameInfo.emulators,
					storedDocument.rickdangerousGameInfo.emulators);
		}

		if (newDocument.vmanGameInfo != null && storedDocument.vmanGameInfo != null) {
			newDocument.vmanGameInfo.emulators = SetUtil.addValue(newDocument.vmanGameInfo.emulators,
					storedDocument.vmanGameInfo.emulators);
		}

		if (newDocument.wolfanozGameInfo != null && storedDocument.wolfanozGameInfo != null) {
			newDocument.wolfanozGameInfo.emulators = SetUtil.addValue(newDocument.wolfanozGameInfo.emulators,
					storedDocument.wolfanozGameInfo.emulators);
		}

		// other
		newDocument.clones = SetUtil.addValue(newDocument.clones, storedDocument.clones);
		newDocument.collections = SetUtil.addValue(newDocument.collections, storedDocument.collections);
		newDocument.input = SetUtil.addValue(newDocument.input, storedDocument.input);
	}
}
