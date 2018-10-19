package com.lazygalaxy.load;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.Match;
import com.lazygalaxy.domain.Team;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.load.jsoup.JSoupLoad;

public class MatchLiveScoreJSoupLoad extends JSoupLoad<Match> {
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public MatchLiveScoreJSoupLoad() {
		super(Match.class);
	}

	@Override
	protected List<Match> getMongoDocuments(Document htmlDocument) throws Exception {
		Elements links = htmlDocument.select("a[class=scorelink]");
		List<Match> matchList = new ArrayList<Match>();
		for (Element link : links) {
			String href = link.attr("href");
			Document linkDocument = getHTMLDocument(href);
			Elements homeElement = linkDocument.select("div[data-type=home-team]");
			Elements awayElement = linkDocument.select("div[data-type=away-team]");

			Team homeTeam = teamHelper.getDocumentByLabel(homeElement.text());
			Team awayTeam = teamHelper.getDocumentByLabel(awayElement.text());
			matchList.add(new Match(null, null, homeTeam, awayTeam, null));
		}
		return matchList;
	}
}