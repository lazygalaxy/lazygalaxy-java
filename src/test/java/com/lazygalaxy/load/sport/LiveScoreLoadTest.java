package com.lazygalaxy.load.sport;

import java.util.List;

import com.lazygalaxy.domain.sport.Match;

import junit.framework.TestCase;

public class LiveScoreLoadTest extends TestCase {
	public void testAll() throws Exception {

		MatchLiveScoreJSoupLoad scraper = new MatchLiveScoreJSoupLoad();
		List<Match> matchList = scraper
				.getMongoDocuments(scraper.getHTMLDocument("html/livescore-football-20180505-fixtures.html"));

		assertEquals(367, matchList.size());
	}

}
