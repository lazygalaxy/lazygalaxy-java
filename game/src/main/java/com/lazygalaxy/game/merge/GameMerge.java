package com.lazygalaxy.game.merge;

import com.lazygalaxy.engine.merge.FieldMerge;
import com.lazygalaxy.game.domain.Game;

public class GameMerge extends FieldMerge<Game> {
	@Override
	public void apply(Game newDocument, Game storedDocument) throws Exception {
		super.apply(newDocument, storedDocument);

		if (newDocument.rickdangerous_ultimateGameInfo != null && newDocument.rickdangerous_ultimateGameInfo.rating != null) {
			if (storedDocument.rickdangerous_ultimateGameInfo != null && storedDocument.rickdangerous_ultimateGameInfo.rating != null) {
				newDocument.rickdangerous_ultimateGameInfo.rating = Math.max(newDocument.rickdangerous_ultimateGameInfo.rating, storedDocument.rickdangerous_ultimateGameInfo.rating);
			}
		}
	}
}
