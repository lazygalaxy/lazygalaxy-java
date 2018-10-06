package com.lazygalaxy.load.sport;

import java.util.Set;

import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.helpers.MongoHelper;

import junit.framework.TestCase;

public class LiveScoreLoadTest extends TestCase {
	public void testAll() throws Exception {
		MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

		MatchLiveScoreJSoupLoad scraper = new MatchLiveScoreJSoupLoad();
		Set<String> links = scraper.getLinks("html/livescore-football-20180505-fixtures.html");

		assertEquals(367, links.size());
		assertEquals(
				"http://www.livescore.com/soccer/england/premier-league/leicester-city-vs-west-ham-united/1-2523108/",
				links.toArray()[2]);

		// Match match =
		// scraper.getMongoDocument("html/livescore-watford-vs-newcastle-20180505.html");
		// assertEquals(teamHelper.getDocumentByLabel("Watford").id,
		// match.homeTeamId);
		// assertEquals(teamHelper.getDocumentByLabel("Newcastle").id,
		// match.awayTeamId);
	}

}
