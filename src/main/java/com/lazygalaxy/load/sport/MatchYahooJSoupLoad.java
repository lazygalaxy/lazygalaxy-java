package com.lazygalaxy.load.sport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.sport.Match;
import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.helpers.MongoHelper;
import com.lazygalaxy.load.JSoupLoad;

public class MatchYahooJSoupLoad extends JSoupLoad<Match> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy, HHmm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public MatchYahooJSoupLoad() {
		super(Match.class);
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

		return new Match(null, dateTime, homeTeam, awayTeam, null);
	}
}