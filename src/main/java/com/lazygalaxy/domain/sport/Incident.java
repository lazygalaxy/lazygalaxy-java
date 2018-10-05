package com.lazygalaxy.domain.sport;

import java.util.Set;
import java.util.TreeSet;

public class Incident implements Comparable<Incident> {
	public enum Type {
		ASSIST, ERROR, GOAL, MISS, LEFT, PENALTY, POST, RED, RIGHT, SAVE, SETPIECE, SUBOFF, SUBON, UNKNOWN, YELLOW;
	}

	public String playerId;
	public String teamId;
	public Integer seconds;
	public Set<Type> types = new TreeSet<Type>();

	public Incident() {
	}

	public Incident(Player player, Team team, Integer seconds, Set<Incident.Type> types) throws Exception {
		this.playerId = player.id;
		this.teamId = team.id;
		this.seconds = seconds;
		this.types = types;
	}

	@Override
	public int compareTo(Incident incident) {
		return seconds.compareTo(incident.seconds);
	}

}
