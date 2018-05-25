package com.lazygalaxy.sport.scrapers;

import java.util.Set;

import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.html.MatchWhoScoredHTMLLoad;

import junit.framework.TestCase;

public class WhoScoredScrapperTest extends TestCase {
	public void testAll() throws Exception {
		MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);
		MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

		MatchWhoScoredHTMLLoad scraper = new MatchWhoScoredHTMLLoad(
				leagueHelper.getDocumentByLabel("English Premier League"));
		Set<String> links = scraper.getLinks("html/whoscored-football-201708-fixtures.html");

		assertEquals(30, links.size());
		assertEquals(
				"https://www.whoscored.com/Matches/1190183/LiveStatistics/England-Premier-League-2017-2018-West-Bromwich-Albion-Bournemouth",
				links.toArray()[2]);

		Match match = scraper.getMongoDocument("html/whoscored-watford-vs-newcastle-20180505.html");
		assertEquals(teamHelper.getDocumentByLabel("Watford").getId(), match.getHomeTeamId());
		assertEquals(teamHelper.getDocumentByLabel("Newcastle").getId(), match.getAwayTeamId());
	}

}
