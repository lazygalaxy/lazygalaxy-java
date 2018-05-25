package com.lazygalaxy.sport.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match extends MongoDocument {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private LocalDateTime dateTime;
	private String homeTeamId;
	private String awayTeamId;

	public Match() {
	}

	public Match(String name, String[] labels, LocalDateTime dateTime, Team homeTeam, Team awayTeam) {
		super(dateTime.format(DATE_TIME_FORMATTER) + "_" + homeTeam.getId() + "_" + awayTeam.getCountryId(), name,
				labels);
		this.dateTime = dateTime;
		this.homeTeamId = homeTeam.getId();
		this.awayTeamId = awayTeam.getId();
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(String homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public String getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(String awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
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
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
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
