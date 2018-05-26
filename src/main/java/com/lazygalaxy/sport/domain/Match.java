package com.lazygalaxy.sport.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match extends MongoDocument {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	public LocalDateTime dateTime;
	public String homeTeamId;
	public String awayTeamId;

	public Match() {
	}

	public Match(String name, String[] labels, LocalDateTime dateTime, Team homeTeam, Team awayTeam) {
		super(dateTime.format(DATE_TIME_FORMATTER) + "_" + homeTeam.id + "_" + awayTeam.countryId, name,
				labels);
		this.dateTime = dateTime;
		this.homeTeamId = homeTeam.id;
		this.awayTeamId = awayTeam.id;
	}
}
