package com.lazygalaxy.load.sport;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.sport.Match;
import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.helpers.MongoHelper;
import com.lazygalaxy.load.JSoupLoad;

public class MatchLiveScoreJSoupLoad extends JSoupLoad<Match> {
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public MatchLiveScoreJSoupLoad() {
		super(Match.class);
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements links = doc.select("a[class=scorelink]");
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

		Elements homeElement = doc.select("div[data-type=home-team]");
		Elements awayElement = doc.select("div[data-type=away-team]");

		Team homeTeam = teamHelper.getDocumentByLabel(homeElement.text());
		Team awayTeam = teamHelper.getDocumentByLabel(awayElement.text());

		return new Match(null, null, homeTeam, awayTeam, null);
	}
}