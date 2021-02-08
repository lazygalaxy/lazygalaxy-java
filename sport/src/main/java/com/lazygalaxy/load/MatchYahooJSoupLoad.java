package com.lazygalaxy.load;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.Match;
import com.lazygalaxy.domain.Team;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.load.jsoup.JSoupLoad;

public class MatchYahooJSoupLoad extends JSoupLoad<Match> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy, HHmm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public MatchYahooJSoupLoad() throws Exception {
		super(Match.class);
	}

	@Override
	public List<Match> getMongoDocuments(Document htmlDocument) throws Exception {
		Elements scoreboard = htmlDocument.select("div[id*=scoreboard-group-2]");
		Elements links = scoreboard.select("a");

		List<Match> matchList = new ArrayList<Match>();
		for (Element link : links) {
			String href = link.attr("href");
			Document linkDocument = getHTMLDocument(href);

			Elements gameStatus = linkDocument.select("div[class*=game-status]");
			Elements matchStats = linkDocument.select("div[class*=match-stats]");
			Elements teams = matchStats.select("span[class=D(ib) Va(m)]");

			LocalDateTime dateTime = LocalDateTime.parse(gameStatus.get(0).child(0).child(0).text() + ", 0000",
					DATE_TIME_FORMATTER);
			Team homeTeam = teamHelper.getDocumentByLabel(teams.get(0).text());
			Team awayTeam = teamHelper.getDocumentByLabel(teams.get(1).text());

			matchList.add(new Match(null, dateTime, homeTeam, awayTeam, null));
		}

		return matchList;
	}
}