package com.lazygalaxy.sport.load;

import java.util.Set;

import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.MatchYahooHTMLLoad;

import junit.framework.TestCase;

public class YahooLoadTest extends TestCase {
	public void testAll() throws Exception {
		MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

		MatchYahooHTMLLoad scraper = new MatchYahooHTMLLoad();
		Set<String> links = scraper.getLinks("html/yahoo-football-week1-fixtures.html");

		assertEquals(10, links.size());
		assertEquals("https://sports.yahoo.com/soccer/premier-league/chelsea-burnley-80000/", links.toArray()[2]);

		// Match match =
		// scraper.getMongoDocument("html/yahoo-watford-vs-newcastle-20180505.html");
		// assertEquals(teamHelper.getDocumentByLabel("Watford").id,
		// match.homeTeamId);
		// assertEquals(teamHelper.getDocumentByLabel("Newcastle").id,
		// match.awayTeamId);
	}

}
