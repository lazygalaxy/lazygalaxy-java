package com.lazygalaxy.sport.load;

import java.time.format.DateTimeFormatter;
import java.util.Set;

import com.lazygalaxy.sport.domain.Country;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Player;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;

import junit.framework.TestCase;

public class WhoScoredLoadTest extends TestCase {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private static final MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);

	public void testMatch() throws Exception {

		MatchWhoScoredHTMLLoad scraper = new MatchWhoScoredHTMLLoad();
		Set<String> links = scraper.getLinks("html/whoscored-football-201708-fixtures.html");

		assertEquals(30, links.size());
		assertEquals(
				"https://www.whoscored.com/Matches/1190183/LiveStatistics/England-Premier-League-2017-2018-West-Bromwich-Albion-Bournemouth",
				links.toArray()[2]);

		Match match = scraper.getMongoDocument("html/whoscored-watford-vs-newcastle-20180505.html");
		assertEquals(teamHelper.getDocumentByLabel("Watford").id, match.homeTeamId);
		assertEquals(teamHelper.getDocumentByLabel("Newcastle").id, match.awayTeamId);
	}

	public void testPlayer1() throws Exception {

		PlayerWhoScoredHTMLLoad scraper = new PlayerWhoScoredHTMLLoad();

		Player player = scraper.getMongoDocument("html/whoscored-football-19921023_alvaromorata.html");
		assertEquals("19921023_alvaromorata", player.id);
		assertEquals("Álvaro Morata", player.name);
		assertEquals("19921023", player.birthDate.format(DATE_TIME_FORMATTER));
		assertEquals(countryHelper.getDocumentById("es").id, player.countryId);
		assertEquals(teamHelper.getDocumentByLabel("Chelsea").id, player.teamId);
		assertEquals(Integer.valueOf(189), player.height);
		assertEquals(Integer.valueOf(85), player.weight);
		assertEquals(Integer.valueOf(91213), player.whoScoredId);
		assertEquals(Player.Position.FW, player.whoScoredPosition);

		assertEquals(2, player.labels.size());
		assertEquals(true, player.labels.contains("alvaromorata"));
		assertEquals(true, player.labels.contains("amorata"));
	}

	public void testPlayer2() throws Exception {

		PlayerWhoScoredHTMLLoad scraper = new PlayerWhoScoredHTMLLoad();

		Player player = scraper.getMongoDocument("html/whoscored-football-19910628_kevindebruyne.html");
		assertEquals("19910628_kevindebruyne", player.id);
		assertEquals("Kevin De Bruyne", player.name);
		assertEquals("19910628", player.birthDate.format(DATE_TIME_FORMATTER));
		assertEquals(countryHelper.getDocumentById("be").id, player.countryId);
		assertEquals(teamHelper.getDocumentByLabel("Manchester City").id, player.teamId);
		assertEquals(Integer.valueOf(181), player.height);
		assertEquals(Integer.valueOf(68), player.weight);
		assertEquals(Integer.valueOf(73084), player.whoScoredId);
		assertEquals(Player.Position.FW, player.whoScoredPosition);

		assertEquals(2, player.labels.size());
		assertEquals(true, player.labels.contains("kevindebruyne"));
		assertEquals(true, player.labels.contains("kdebruyne"));
	}

}
