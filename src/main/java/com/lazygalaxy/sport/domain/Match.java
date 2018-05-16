package com.lazygalaxy.sport.domain;

import org.bson.types.ObjectId;

public class Match extends MongoDocument {
	private ObjectId homeTeamId;
	private ObjectId awayTeamId;

	public Match(String name, String[] labels, Team homeTeam, Team awayTeam) {
		super(name, labels);
		this.homeTeamId = homeTeam.getId();
		this.awayTeamId = awayTeam.getId();
	}

	public ObjectId getHomeTeamId() {
		return homeTeamId;
	}

	public ObjectId getAwayTeamId() {
		return awayTeamId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((homeTeamId == null) ? 0 : homeTeamId.hashCode());
		result = prime * result + ((awayTeamId == null) ? 0 : awayTeamId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (homeTeamId == null) {
			if (other.homeTeamId != null)
				return false;
		} else if (!homeTeamId.equals(other.homeTeamId))
			return false;
		if (awayTeamId == null) {
			if (other.awayTeamId != null)
				return false;
		} else if (!awayTeamId.equals(other.awayTeamId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " " + homeTeamId + " " + awayTeamId;
	}
}
