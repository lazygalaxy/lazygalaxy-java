package com.lazygalaxy.load.sport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.domain.sport.Player;
import com.lazygalaxy.domain.sport.Team;
import com.lazygalaxy.domain.world.Country;
import com.lazygalaxy.helpers.MongoHelper;
import com.lazygalaxy.load.JSoupLoad;
import com.lazygalaxy.utils.GeneralUtil;

public class PlayerWhoScoredJSoupLoad extends JSoupLoad<Player> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private final MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public PlayerWhoScoredJSoupLoad() {
		super(Player.class);
	}

	@Override
	public Set<String> getLinks(String html) throws Exception {
		throw new Exception("no implementation");
	}

	@Override
	public Player getMongoDocument(String html) throws Exception {
		Document doc = getHTMLDocument(html);

		Elements playerInfoBlockElements = doc.select("dl[class=player-info-block]");

		LocalDate birthDate = null;
		String name = null;
		Country country = null;
		Integer whoScoredId = null;
		Integer height = null;
		Integer weight = null;
		Team team = null;
		Player.Position whoScoredPosition = null;

		for (Element element : playerInfoBlockElements) {
			if (StringUtils.startsWith(element.select("dt").text(), "Age")) {
				String value = element.select("dd").text();
				birthDate = LocalDate.parse(value.substring(value.indexOf('(') + 1, value.length() - 1),
						DATE_TIME_FORMATTER);
			} else if (StringUtils.startsWith(element.select("dt").text(), "Name")) {
				name = element.select("dd").text();
			} else if (StringUtils.startsWith(element.select("dt").text(), "Nationality")) {
				country = countryHelper.getDocumentByLabel(element.select("dd").text());
			} else if (StringUtils.startsWith(element.select("dt").text(), "Height")) {
				height = Integer.parseInt(GeneralUtil.numerify(element.select("dd").text()));
			} else if (StringUtils.startsWith(element.select("dt").text(), "Weight")) {
				weight = Integer.parseInt(GeneralUtil.numerify(element.select("dd").text()));
			} else if (StringUtils.startsWith(element.select("dt").text(), "Current Team")) {
				team = teamHelper.getDocumentByLabel(element.select("dd").text());
			} else if (StringUtils.startsWith(element.select("dt").text(), "Position")) {
				whoScoredPosition = Player.Position.getPosition(element.select("dd").text());
			}
		}

		Element playerPictureElement = doc.select("img[class=player-picture]").get(0);
		String playerPictureLink = playerPictureElement.attr("src");
		int index = playerPictureLink.lastIndexOf('/');
		whoScoredId = Integer.parseInt(playerPictureLink.substring(index + 1, playerPictureLink.length() - 4));

		return new Player(birthDate, name, country, whoScoredId, height, weight, team, whoScoredPosition);
	}
}