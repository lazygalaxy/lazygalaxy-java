package com.lazygalaxy.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.domain.Game;

public enum Collection {

	ARKANOID("Arkanoid"), //
	BUBBLE_BOBBLE("Bubble Bobble"), //
	DOUBLE_DRAGON("Double Dragon"), //
	GOLDEN_AXE("Golden Axe"), //
	METAL_SLUG("Metal Slug"), //
	MORTAL_KOMBAT("Mortal Kombat"), //
	PACMAN("Pac-Man", "Puckman"), //
	RAMPAGE("Rampage"), //
	STAR_WARS("Star Wars"), //
	STREET_FIGHTER("Street Fighter"), //
	SUPER_MARIO("Super Mario", "Mario Bro"), //
	SUPER_HEROES("Super Heroes", "Batman", "Captain America", "Spider-Man", "Superman", "The Punisher", "X-Men"), //
	TETRIS("Tetris"); //

	public static Set<String> get(Game game) {
		Set<String> collections = null;
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
		return collections;
	}

	private List<String> labelSet = new ArrayList<String>();

	Collection(String... labels) {
		for (String label : labels) {
			addLabel(label);
		}
	}

	String getMainLabel() {
		return this.labelSet.get(0);
	}

	void addLabel(String label) {
		labelSet.add(GeneralUtil.alphanumerify(label));
	}
}
