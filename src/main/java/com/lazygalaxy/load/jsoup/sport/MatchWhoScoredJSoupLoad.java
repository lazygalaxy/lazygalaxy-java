package com.lazygalaxy.load.jsoup.sport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.sport.Incident;
import com.lazygalaxy.domain.sport.League;
import com.lazygalaxy.domain.sport.Match;
import com.lazygalaxy.domain.sport.Player;
import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.helpers.MongoHelper;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.utils.sport.WhoScoredUtil;

public class MatchWhoScoredJSoupLoad extends JSoupLoad<Match> {
	private static final Logger LOGGER = LogManager.getLogger(MatchWhoScoredJSoupLoad.class);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd-MMM-yy, HH:mm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);
	private final MongoHelper<Player> playerHelper = MongoHelper.getHelper(Player.class);

	private final PlayerWhoScoredJSoupLoad playerHTMLLoad = new PlayerWhoScoredJSoupLoad();

	public MatchWhoScoredJSoupLoad() {
		super(Match.class);
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements matchreport = doc.select("a[class*=match-report]");
		Set<String> linkSet = new LinkedHashSet<String>();
		for (Element link : matchreport) {
			String href = link.attr("href");
			href = href.replace("/MatchReport/", "/LiveStatistics/");
			linkSet.add(href);
		}

		return linkSet;
	}

	@Override
	public Match getMongoDocument(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements infoBlockElements = doc.select("div[class*=info-block]");
		Element lastInfoBlockElement = infoBlockElements.get(infoBlockElements.size() - 1);
		Elements dateTimeElements = lastInfoBlockElement.select("dd");
		LocalDateTime dateTime = LocalDateTime
				.parse(dateTimeElements.get(1).text() + ", " + dateTimeElements.get(0).text(), DATE_TIME_FORMATTER);

		Elements teamElement = doc.select("td[class*=team]");
		Element homeTeamElement = teamElement.get(0);
		Element awayTeamElement = teamElement.get(1);
		Team homeTeam = teamHelper.getDocumentByLabel(homeTeamElement.text());
		Team awayTeam = teamHelper.getDocumentByLabel(awayTeamElement.text());

		Element navigatorElement = doc.select("div[id=breadcrumb-nav]").get(0);
		String[] leagueLink = navigatorElement.select("a").get(0).attr("href").split("/");

		Integer whoScoredId = Integer.parseInt(leagueLink[leagueLink.length - 4]);
		League league = leagueHelper.getDocumentByField("whoScoredId", whoScoredId);

		Set<Incident> incidentSet = new TreeSet<Incident>();

		Elements playerStatsElements = doc.select("tbody[id=player-table-statistics-body]");
		for (Element playerStatsElement : playerStatsElements) {
			Elements playerStatsRowElements = playerStatsElement.select("td");
			for (Element playerStatsRowElement : playerStatsRowElements) {
				Elements incidentElements = playerStatsRowElement.select("span[class=incident-icon]");
				for (Element incidentElement : incidentElements) {
					if (incidentElement.hasAttr("data-player-id")) {
						Integer minute = Integer.parseInt(incidentElement.attr("data-minute"));
						Integer second = Integer.parseInt(incidentElement.attr("data-second"));
						Integer teamId = Integer.parseInt(incidentElement.attr("data-team-id"));
						Integer playerId = Integer.parseInt(incidentElement.attr("data-player-id"));

						Team team = teamHelper.getDocumentByField("whoScoredId", teamId);
						// TODO: we could apply something similar here as the
						// player
						Player player = playerHelper.getDocumentByField("whoScoredId", playerId);
						if (player == null) {
							player = playerHTMLLoad.getMongoDocument(WhoScoredUtil.getPlayerURL(playerId));
							playerHelper.upsert(player);
						}

						Set<Incident.Type> types = new TreeSet<Incident.Type>();
						if (incidentElement.hasAttr("data-event-satisfier-assist")) {
							types.add(Incident.Type.ASSIST);
						}
						if (incidentElement.hasAttr("data-event-satisfier-errorleadstogoal")) {
							types.add(Incident.Type.ERROR);
						}
						if (incidentElement.hasAttr("data-event-satisfier-goalnormal")) {
							types.add(Incident.Type.GOAL);
						}
						if (incidentElement.hasAttr("data-event-satisfier-shotleftfoot")) {
							types.add(Incident.Type.LEFT);
						}
						if (incidentElement.hasAttr("data-event-satisfier-penaltymissed")) {
							types.add(Incident.Type.PENALTY);
							types.add(Incident.Type.MISS);
						}
						if (incidentElement.hasAttr("data-event-satisfier-keeperpenaltysaved")) {
							types.add(Incident.Type.PENALTY);
							types.add(Incident.Type.SAVE);
						}
						if (incidentElement.hasAttr("data-event-satisfier-shotonpost")) {
							types.add(Incident.Type.POST);
						}
						if (incidentElement.hasAttr("data-event-satisfier-redcard")) {
							types.add(Incident.Type.RED);
						}
						if (incidentElement.hasAttr("data-event-satisfier-shotrightfoot")) {
							types.add(Incident.Type.RIGHT);
						}
						if (incidentElement.hasAttr("data-event-satisfier-suboff")) {
							types.add(Incident.Type.SUBOFF);
						}
						if (incidentElement.hasAttr("data-event-satisfier-subon")) {
							types.add(Incident.Type.SUBON);
						}
						if (incidentElement.hasAttr("data-event-satisfier-shotsetpiece")) {
							types.add(Incident.Type.SETPIECE);
						}
						if (incidentElement.hasAttr("data-event-satisfier-yellowcard")
								|| incidentElement.hasAttr("data-event-satisfier-secondyellow")) {
							types.add(Incident.Type.YELLOW);
						}
						if (types.size() > 0) {
							Incident incident = new Incident(player, team, (minute * 60) + second, types);
							incidentSet.add(incident);
						} else {
							LOGGER.error("no incidents captured: " + incidentElement);
						}
					}
				}
			}
		}

		return new Match(league, dateTime, homeTeam, awayTeam, incidentSet);
	}
}