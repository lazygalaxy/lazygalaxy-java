package com.lazygalaxy.sport.load.html;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;

public class MatchWhoScoredHTMLLoad extends HTMLLoad<Match> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd-MMM-yy, HH:mm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final League[] leagues;

	public MatchWhoScoredHTMLLoad(League... leagues) {
		super(Match.class);
		this.leagues = leagues;
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements matchreport = doc.select("a[class*=match-report]");
		Set<String> linkSet = new LinkedHashSet<String>();
		for (Element link : matchreport) {
			String href = link.attr("href");
			for (League league : leagues) {
				if (href.contains(league.getWhoScoredCode())) {
					href = href.replace("/MatchReport/", "/LiveStatistics/");
					linkSet.add(href);
					break;
				}
			}
		}

		return linkSet;
	}

	@Override
	public Match getMongoDocument(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements infoBlockElements = doc.select("div[class*=info-block]");
		Element lastInfoBlockElement = infoBlockElements.get(infoBlockElements.size() - 1);
		Elements dateTimeElements = lastInfoBlockElement.select("dd");
		LocalDateTime dateTime = LocalDateTime
				.parse(dateTimeElements.get(1).text() + ", " + dateTimeElements.get(0).text(), DATE_TIME_FORMATTER);

		Elements teamElement = doc.select("td[class*=team]");
		Element homeTeamElement = teamElement.get(0);
		Element awayTeamElement = teamElement.get(1);
		Team homeTeam = teamHelper.getDocumentByLabel(homeTeamElement.text());
		Team awayTeam = teamHelper.getDocumentByLabel(awayTeamElement.text());

		Elements playerStatElements = doc.select("tbody[id=player-table-statistics-body]");
		Element homeStatsElement = playerStatElements.get(0);
		Element awayStatsElement = playerStatElements.get(1);

		return new Match(homeTeam.getName() + awayTeam.getName(), new String[] {}, dateTime, homeTeam, awayTeam);
	}
}