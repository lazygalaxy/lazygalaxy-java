package com.lazygalaxy.sport.load.html;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;

public class MatchLiveScoreHTMLLoad extends HTMLLoad<Match> {
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final League[] leagues;

	public MatchLiveScoreHTMLLoad(League... leagues) {
		super(Match.class);
		this.leagues = leagues;
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

		return new Match(homeTeam.name + awayTeam.name, new String[] {}, null, homeTeam, awayTeam);
	}
}