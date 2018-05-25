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
}
