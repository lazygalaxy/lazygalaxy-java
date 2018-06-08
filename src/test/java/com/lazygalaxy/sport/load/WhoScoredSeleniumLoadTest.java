package com.lazygalaxy.sport.load;

import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.lazygalaxy.sport.domain.Incident.Type;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.selenium.MatchWhoScoredSeleniumLoad;

import junit.framework.TestCase;

public class WhoScoredSeleniumLoadTest extends TestCase {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmSS");
	private static final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public void testMatch() throws Exception {

		MatchWhoScoredSeleniumLoad scraper = new MatchWhoScoredSeleniumLoad();
		Set<String> links = scraper.getLinks("html/whoscored-football-201708-fixtures.html", 10);

		assertEquals(30, links.size());
		assertEquals(
				"https://www.whoscored.com/Matches/1190183/LiveStatistics/England-Premier-League-2017-2018-West-Bromwich-Albion-Bournemouth",
				links.toArray()[2]);

		Match match = scraper.getMongoDocument("html/match/201805051500_enwatford_ennewcastleunited.html", true);
		assertEquals("enpremierleague", match.leagueId);
		assertEquals("20180505150000", match.dateTime.format(DATE_TIME_FORMATTER));
		assertEquals(teamHelper.getDocumentByLabel("Watford").id, match.homeTeamId);
		assertEquals(teamHelper.getDocumentByLabel("Newcastle").id, match.awayTeamId);
		assertEquals(17, match.incidentSet.size());
		Pair<Integer, Integer> goalStats = match.getMatchStats(Type.GOAL);
		assertEquals(2, goalStats.getLeft().intValue());
		assertEquals(1, goalStats.getRight().intValue());
	}
}
