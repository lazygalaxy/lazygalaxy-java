package com.lazygalaxy.sport.load;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.jsoup.JSoupLoad;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;

public class MatchLiveScoreJSoupLoad extends JSoupLoad<Match> {
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public MatchLiveScoreJSoupLoad() throws Exception {
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

			List<Team> homeTeam = teamHelper.getDocumentsByLabel(homeElement.text());
			List<Team> awayTeam = teamHelper.getDocumentsByLabel(awayElement.text());
			matchList.add(new Match(null, null, homeTeam.get(0), awayTeam.get(0), null));
		}
		return matchList;
	}
}