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

public class MatchYahooHTMLLoad extends HTMLLoad<Match> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy, HHmm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final League[] leagues;

	public MatchYahooHTMLLoad(League... leagues) {
		super(Match.class);
		this.leagues = leagues;
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements scoreboard = doc.select("div[id*=scoreboard-group-2]");
		Elements links = scoreboard.select("a");
		Set<String> linkSet = new LinkedHashSet<String>();
		for (Element link : links) {
			String href = link.attr("href");
			linkSet.add(href);
		}

		return linkSet;
	}

	@Override
	public Match getMongoDocument(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements gameStatus = doc.select("div[class*=game-status]");
		Elements matchStats = doc.select("div[class*=match-stats]");
		Elements teams = matchStats.select("span[class=D(ib) Va(m)]");

		LocalDateTime dateTime = LocalDateTime.parse(gameStatus.get(0).child(0).child(0).text() + ", 0000",
				DATE_TIME_FORMATTER);
		Team homeTeam = teamHelper.getDocumentByLabel(teams.get(0).text());
		Team awayTeam = teamHelper.getDocumentByLabel(teams.get(1).text());

		return new Match(homeTeam.getName() + awayTeam.getName(), new String[] {}, dateTime, homeTeam, awayTeam);
	}
}