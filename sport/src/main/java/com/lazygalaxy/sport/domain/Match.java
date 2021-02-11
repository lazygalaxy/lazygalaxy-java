package com.lazygalaxy.sport.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.lazygalaxy.engine.domain.MongoDocument;

public class Match extends MongoDocument {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	public String leagueId;
	public LocalDateTime dateTime;
	public String homeTeamId;
	public String awayTeamId;
	public Set<Incident> incidentSet;

	public Match() {
	}

	public Match(League league, LocalDateTime dateTime, Team homeTeam, Team awayTeam, Set<Incident> incidentSet)
			throws Exception {
		super(buildId("_", dateTime.format(DATE_TIME_FORMATTER), homeTeam.id, awayTeam.id), null, null);
		this.leagueId = league.id;
		this.dateTime = dateTime;
		this.homeTeamId = homeTeam.id;
		this.awayTeamId = awayTeam.id;
		this.incidentSet = incidentSet;
	}

	public Pair<Integer, Integer> getMatchStats(Incident.Type incidentType) {
		Integer homeStat = 0;
		Integer awayStat = 0;
		for (Incident incident : incidentSet) {
			if (incident.types.contains(incidentType)) {
				if (StringUtils.equals(homeTeamId, incident.teamId)) {
					homeStat++;
				} else if (StringUtils.equals(awayTeamId, incident.teamId)) {
					awayStat++;
				}
			}
		}
		return new ImmutablePair<Integer, Integer>(homeStat, awayStat);
	}
}
