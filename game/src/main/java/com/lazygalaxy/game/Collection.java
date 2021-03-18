package com.lazygalaxy.game;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;

public enum Collection {
	ARKANOID("Arkanoid"), //
	BUBBLE_BOBBLE("Bubble Bobble"), //
	DOUBLE_DRAGON("Double Dragon"), //
	FOUR_PLAYER(false, "4 Player"), //
	GOLDEN_AXE("Golden Axe"), //
	METAL_SLUG("Metal Slug"), //
	MORTAL_KOMBAT("Mortal Kombat"), //
	MOVIES(false, "Movies", "RoboCop", "Terminator"), //
	PACMAN("Pac-Man", "Puckman"), //
	RAMPAGE("Rampage"), //
	STAR_WARS("Star Wars"), //
	STREET_FIGHTER("Street Fighter"), //
	SUPER_MARIO("Super Mario", "Mario Bro"), //
	SUPER_HEROES(false, "Super Heroes", "Batman", "Captain America", "Spider-Man", "Superman", "The Punisher", "X-Men"), //
	TETRIS("Tetris"), //
	THREE_PLAYER(false, "3 Player"), //
	VERTICAL(false, "Vertical"); //

	public static TreeSet<String> get(Game game) {
		TreeSet<String> collections = null;
		String name = GeneralUtil.alphanumerify(game.name);
		for (Collection collection : Collection.values()) {
			for (String label : collection.labelSet) {
				if (StringUtils.contains(name, label)) {
					if (collections == null) {
						collections = new TreeSet<String>();
					}
					collections.add(collection.getMainLabel());
				}
			}
		}

		if (game.isVeritcal) {
			if (collections == null) {
				collections = new TreeSet<String>();
			}
			collections.add(VERTICAL.getMainLabel());
		}

		if (game.players >= 3) {
			if (collections == null) {
				collections = new TreeSet<String>();
			}
			collections.add(THREE_PLAYER.getMainLabel());
		}

		if (game.players >= 4) {
			if (collections == null) {
				collections = new TreeSet<String>();
			}
			collections.add(FOUR_PLAYER.getMainLabel());
		}

		return collections;
	}

	private List<String> labelSet = new ArrayList<String>();
	private String mainLabel;
	private boolean considerMainLabel;

	Collection(String... labels) {
		this(true, labels);
	}

	Collection(boolean considerMainLabel, String... labels) {
		this.considerMainLabel = considerMainLabel;
		this.mainLabel = labels[0];
		for (int i = considerMainLabel ? 0 : 1; i < labels.length; i++) {
			addLabel(labels[i]);
		}
	}

	String getMainLabel() {
		return mainLabel;
	}

	void addLabel(String label) {
		labelSet.add(GeneralUtil.alphanumerify(label));
	}
}
