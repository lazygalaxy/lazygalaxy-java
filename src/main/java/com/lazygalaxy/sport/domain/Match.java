package com.lazygalaxy.sport.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match extends MongoDocument {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	public String leagueId;
	public LocalDateTime dateTime;
	public String homeTeamId;
	public String awayTeamId;

	public Match() {
	}

	public Match(League league, LocalDateTime dateTime, Team homeTeam, Team awayTeam) throws Exception {
		super(buildId("_", dateTime.format(DATE_TIME_FORMATTER), homeTeam.id, awayTeam.id), null, null);
		this.leagueId = league.id;
		this.dateTime = dateTime;
		this.homeTeamId = homeTeam.id;
		this.awayTeamId = awayTeam.id;
	}
}
