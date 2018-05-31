package com.lazygalaxy.sport.domain;

import java.util.Set;
import java.util.TreeSet;

public class Event extends MongoDocument {
	public enum Type {
		ASSIST, GOAL, MISS, PENALTY, RED, SAVE, YELLOW;
	}

	public String matchId;
	public String playerId;
	public Integer seconds;
	public Set<Type> types = new TreeSet<Type>();

	public Event() {
	}

	public Event(Match match, Player player, Integer seconds, Type[] typeArray) {
		super(match + "_" + player + "_" + seconds, null, null);
		this.matchId = match.id;
		this.playerId = player.id;
		this.seconds = seconds;
		for (Type type : typeArray) {
			types.add(type);
		}
	}
}
