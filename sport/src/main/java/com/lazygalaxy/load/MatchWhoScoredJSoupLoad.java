package com.lazygalaxy.load;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.Incident;
import com.lazygalaxy.domain.League;
import com.lazygalaxy.domain.Match;
import com.lazygalaxy.domain.Player;
import com.lazygalaxy.domain.Team;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.load.jsoup.JSoupLoad;
import com.lazygalaxy.util.WhoScoredUtil;

public class MatchWhoScoredJSoupLoad extends JSoupLoad<Match> {
	private static final Logger LOGGER = LogManager.getLogger(MatchWhoScoredJSoupLoad.class);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd-MMM-yy, HH:mm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);
	private final MongoHelper<Player> playerHelper = MongoHelper.getHelper(Player.class);

	private final PlayerWhoScoredJSoupLoad playerHTMLLoad = new PlayerWhoScoredJSoupLoad();

	public MatchWhoScoredJSoupLoad() throws Exception {
		super(Match.class);
	}

	@Override
	public List<Match> getMongoDocuments(Document htmlDocument) throws Exception {

		Elements matchreport = htmlDocument.select("a[class*=match-report]");
		List<Match> macthList = new ArrayList<Match>();
		for (Element link : matchreport) {
			String href = link.attr("href");
			href = href.replace("/MatchReport/", "/LiveStatistics/");
			Document linkDocument = getHTMLDocument(href);

			Elements infoBlockElements = linkDocument.select("div[class*=info-block]");
			Element lastInfoBlockElement = infoBlockElements.get(infoBlockElements.size() - 1);
			Elements dateTimeElements = lastInfoBlockElement.select("dd");
			LocalDateTime dateTime = LocalDateTime
					.parse(dateTimeElements.get(1).text() + ", " + dateTimeElements.get(0).text(), DATE_TIME_FORMATTER);

			Elements teamElement = linkDocument.select("td[class*=team]");
			Element homeTeamElement = teamElement.get(0);
			Element awayTeamElement = teamElement.get(1);
			Team homeTeam = teamHelper.getDocumentByLabel(homeTeamElement.text());
			Team awayTeam = teamHelper.getDocumentByLabel(awayTeamElement.text());

			Element navigatorElement = linkDocument.select("div[id=breadcrumb-nav]").get(0);
			String[] leagueLink = navigatorElement.select("a").get(0).attr("href").split("/");

			Integer whoScoredId = Integer.parseInt(leagueLink[leagueLink.length - 4]);
			League league = leagueHelper.getDocumentByField("whoScoredId", whoScoredId);

			Set<Incident> incidentSet = new TreeSet<Incident>();

			Elements playerStatsElements = linkDocument.select("tbody[id=player-table-statistics-body]");
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
							// TODO: we could apply something similar here as
							// the player
							Player player = playerHelper.getDocumentByField("whoScoredId", playerId);
							if (player == null) {
								List<Player> playerList = playerHTMLLoad.getMongoDocuments(
										playerHTMLLoad.getHTMLDocument(WhoScoredUtil.getPlayerURL(playerId)));
								for (Player playerOther : playerList) {
									playerHelper.upsert(playerOther);
								}
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
			macthList.add(new Match(league, dateTime, homeTeam, awayTeam, incidentSet));
		}
		return macthList;
	}
}