package com.lazygalaxy.sport.scrapers;

import java.util.Set;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.html.MatchLiveScoreHTMLLoad;

import junit.framework.TestCase;

/**
 * Example program to list links from a URL.
 */
public class LiveScoreScrapperTest extends TestCase {
	public void testAll() throws Exception {
		MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);
		MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

		MatchLiveScoreHTMLLoad scraper = new MatchLiveScoreHTMLLoad(
				leagueHelper.getDocumentByLabel("English Premier League"));
		Set<String> links = scraper.getLinks("html/livescore-soccer-20180505-fixtures.html");

		assertEquals(6, links.size());
		assertEquals(
				"http://www.livescore.com/soccer/england/premier-league/leicester-city-vs-west-ham-united/1-2523108/",
				links.toArray()[2]);

		Match match = scraper.getMongoDocument("html/livescore-watford-vs-newcastle-20180505.html");
		assertEquals(teamHelper.getDocumentByLabel("Watford").getId(), match.getHomeTeamId());
		assertEquals(teamHelper.getDocumentByLabel("Newcastle").getId(), match.getAwayTeamId());
	}

}
