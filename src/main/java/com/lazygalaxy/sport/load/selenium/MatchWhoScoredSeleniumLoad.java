package com.lazygalaxy.sport.load.selenium;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lazygalaxy.sport.domain.Incident;
import com.lazygalaxy.sport.domain.League;
import com.lazygalaxy.sport.domain.Match;
import com.lazygalaxy.sport.domain.Player;
import com.lazygalaxy.sport.domain.Team;
import com.lazygalaxy.sport.helpers.MongoHelper;
import com.lazygalaxy.sport.load.jsoup.PlayerWhoScoredJSoupLoad;
import com.lazygalaxy.sport.utils.WhoScoredUtil;

public class MatchWhoScoredSeleniumLoad extends SeleniumLoad<Match> {
	private static final Logger LOGGER = LogManager.getLogger(MatchWhoScoredSeleniumLoad.class);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd-MMM-yy, HH:mm");

	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);
	private final MongoHelper<League> leagueHelper = MongoHelper.getHelper(League.class);
	private final MongoHelper<Player> playerHelper = MongoHelper.getHelper(Player.class);

	private final PlayerWhoScoredJSoupLoad playerSeleniumLoad = new PlayerWhoScoredJSoupLoad();

	public MatchWhoScoredSeleniumLoad() {
		super(Match.class);
	}

	@Override
	public Set<String> getLinks(String html, int minLinks) throws Exception {
		WebDriver driver = getHTMLDocument(html);

		Set<String> linkSet = new LinkedHashSet<String>();
		String currentText = null;
		String nextDateText = null;
		do {
			currentText = driver.findElement(By.xpath("//*[@id='date-config-toggle-button']/span[1]")).getText();
			LOGGER.info(currentText);

			final List<WebElement> linkElements = driver.findElements(By.xpath("//a[contains(@class,'match-report')]"));

			for (WebElement linkElement : linkElements) {
				String href = linkElement.getAttribute("href");
				href = href.replace("/MatchReport/", "/LiveStatistics/");
				LOGGER.info(href);
				linkSet.add(href);
			}

			WebElement clickElement = driver.findElement(By.xpath("//*[@id='date-controller']/a[1]/span"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", clickElement);

			nextDateText = driver.findElement(By.xpath("//*[@id='date-config-toggle-button']/span[1]")).getText();
		} while (linkSet.size() <= minLinks && !StringUtils.equals(currentText, nextDateText));

		return linkSet;
	}

	@Override
	public Match getMongoDocument(String html, boolean complete) throws Exception {
		WebDriver driver = getHTMLDocument(html);

		List<WebElement> infoBlockElements = driver.findElements(By.xpath("//div[contains(@class,'info-block')]"));
		WebElement lastInfoBlockElement = infoBlockElements.get(infoBlockElements.size() - 1);
		List<WebElement> dateTimeElements = lastInfoBlockElement.findElements(By.tagName("dd"));
		LocalDateTime dateTime = LocalDateTime.parse(
				dateTimeElements.get(1).getText() + ", " + dateTimeElements.get(0).getText(), DATE_TIME_FORMATTER);

		List<WebElement> teamElement = driver.findElements(By.xpath("//td[contains(@class,'team')]"));
		WebElement homeTeamElement = teamElement.get(0);
		WebElement awayTeamElement = teamElement.get(1);
		Team homeTeam = teamHelper.getDocumentByLabel(homeTeamElement.getText());
		Team awayTeam = teamHelper.getDocumentByLabel(awayTeamElement.getText());

		WebElement navigatorElement = driver.findElements(By.xpath("//div[contains(@id,'breadcrumb-nav')]")).get(0);
		String[] leagueLink = navigatorElement.findElements(By.tagName("a")).get(0).getAttribute("href").split("/");

		Integer whoScoredId = Integer.parseInt(leagueLink[leagueLink.length - 4]);
		League league = leagueHelper.getDocumentByField("whoScoredId", whoScoredId);

		Set<Incident> incidentSet = new TreeSet<Incident>();

		if (complete) {
			List<WebElement> playerStatsElements = driver.findElements(
					By.xpath("//div[@id='live-player-home-summary'] | //div[@id='live-player-away-summary']"));
			for (WebElement playerStatsElement : playerStatsElements) {
				List<WebElement> playerStatsRowElements = playerStatsElement.findElements(By.tagName("tr"));
				for (WebElement playerStatsRowElement : playerStatsRowElements) {
					List<WebElement> incidentElements = playerStatsRowElement.findElements(By.tagName("span"));
					for (WebElement incidentElement : incidentElements) {
						if (incidentElement.getAttribute("data-player-id") != null) {
							Integer minute = Integer.parseInt(incidentElement.getAttribute("data-minute"));
							Integer second = Integer.parseInt(incidentElement.getAttribute("data-second"));
							Integer teamId = Integer.parseInt(incidentElement.getAttribute("data-team-id"));
							Integer playerId = Integer.parseInt(incidentElement.getAttribute("data-player-id"));

							Team team = teamHelper.getDocumentByField("whoScoredId", teamId);
							// TODO: we could apply something similar to teams
							// as we
							// do for players
							Player player = playerHelper.getDocumentByField("whoScoredId", playerId);
							if (player == null) {
								player = playerSeleniumLoad.getMongoDocument(WhoScoredUtil.getPlayerURL(playerId));
								playerHelper.upsert(player);
							}

							Set<Incident.Type> types = new TreeSet<Incident.Type>();
							if (incidentElement.getAttribute("data-event-satisfier-assist") != null) {
								types.add(Incident.Type.ASSIST);
							}
							if (incidentElement.getAttribute("data-event-satisfier-errorleadstogoal") != null) {
								types.add(Incident.Type.ERROR);
							}
							if (incidentElement.getAttribute("data-event-satisfier-goalnormal") != null) {
								types.add(Incident.Type.GOAL);
							}
							if (incidentElement.getAttribute("data-event-satisfier-shotleftfoot") != null) {
								types.add(Incident.Type.LEFT);
							}
							if (incidentElement.getAttribute("data-event-satisfier-penaltymissed") != null) {
								types.add(Incident.Type.PENALTY);
								types.add(Incident.Type.MISS);
							}
							if (incidentElement.getAttribute("data-event-satisfier-keeperpenaltysaved") != null) {
								types.add(Incident.Type.PENALTY);
								types.add(Incident.Type.SAVE);
							}
							if (incidentElement.getAttribute("data-event-satisfier-shotonpost") != null) {
								types.add(Incident.Type.POST);
							}
							if (incidentElement.getAttribute("data-event-satisfier-redcard") != null) {
								types.add(Incident.Type.RED);
							}
							if (incidentElement.getAttribute("data-event-satisfier-shotrightfoot") != null) {
								types.add(Incident.Type.RIGHT);
							}
							if (incidentElement.getAttribute("data-event-satisfier-suboff") != null) {
								types.add(Incident.Type.SUBOFF);
							}
							if (incidentElement.getAttribute("data-event-satisfier-subon") != null) {
								types.add(Incident.Type.SUBON);
							}
							if (incidentElement.getAttribute("data-event-satisfier-shotsetpiece") != null) {
								types.add(Incident.Type.SETPIECE);
							}
							if (incidentElement.getAttribute("data-event-satisfier-yellowcard") != null
									|| incidentElement.getAttribute("data-event-satisfier-secondyellow") != null) {
								types.add(Incident.Type.YELLOW);
							}
							if (types.size() > 0) {
								Incident incident = new Incident(player, team, (minute * 60) + second, types);
								incidentSet.add(incident);
								LOGGER.info(incident.playerId);
							} else {
								LOGGER.error("no incidents captured: " + incidentElement);
							}
						}
					}
				}
			}
		}

		return new Match(league, dateTime, homeTeam, awayTeam, incidentSet);
	}
}