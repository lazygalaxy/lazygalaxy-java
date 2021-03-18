package com.lazygalaxy.game.domain;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.util.GeneralUtil;

public class Game extends MongoDocument {
	// TODO: get database from domain
	public static final String DATABASE = "game";
	public static final String[] EMPTY_LIST = new String[0];

	// master rom mame file
	public String rom;
	public TreeSet<String> romSets;

	// master enrich mame file
	public String originalName;
	public String sourceFile;
	public String cloneOf;
	public TreeSet<String> clones;
	public String romOf;
	public String sampleOf;
	public Boolean isMechanical;

	public String year;
	public String status;
	public TreeSet<String> manufacturers;
	public Boolean isVeritcal;
	public Integer players;
	public Integer coins;
	public TreeSet<String> input;
	public Integer buttons;

	// derived from master mame file
	public String systemId;
	public TreeSet<String> collections;

	// enriched from other sources
	public String description;
	public TreeSet<String> genres;
	public Double rating;
	public Boolean hide;

	public Game() {
	}

	public Game(String id) throws Exception {
		super(id, null, null);
	}

	public Game(String id, String name) throws Exception {
		super(id, name, EMPTY_LIST);
	}

	public Game(String id, String name, String[] labels) throws Exception {
		super(id, name, labels);
	}

	// TODO: create a special collection
	public boolean hasClones() {
		return this.clones != null && this.clones.size() > 0;
	}

	public boolean containsClone(String clone) {
		if (hasClones()) {
			return this.clones.contains(clone);
		}
		return false;
	}

	public void addClone(String... clones) {
		if (this.clones == null) {
			this.clones = new TreeSet<String>();
		}
		for (String clone : clones) {
			this.clones.add(clone);
		}
	}

	public boolean hasRomSets() {
		return this.romSets != null && this.romSets.size() > 0;
	}

	public void addRomSet(String... romSets) {
		if (this.romSets == null) {
			this.romSets = new TreeSet<String>();
		}

		for (String romSet : romSets) {
			this.romSets.add(romSet);
		}
	}

	public void addRomSet(Set<String> romSets) {
		if (this.romSets == null) {
			this.romSets = new TreeSet<String>(romSets);
		} else {
			for (String romSet : romSets) {
				this.romSets.add(romSet);
			}
		}
	}

	public boolean hasGenres() {
		return this.genres != null && this.genres.size() > 0;
	}

	public void addGenre(String... genres) {
		if (this.genres == null) {
			this.genres = new TreeSet<String>();
		}

		for (String genre : genres) {
			this.genres.add(GeneralUtil.alphanumerify(genre));
		}
	}

	public void addGenre(Set<String> genres) {
		if (this.genres == null) {
			this.genres = new TreeSet<String>(genres);
		} else {
			for (String genre : genres) {
				this.genres.add(GeneralUtil.alphanumerify(genre));
			}
		}
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "description");
	}
}
