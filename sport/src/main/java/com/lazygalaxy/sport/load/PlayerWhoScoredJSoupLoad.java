package com.lazygalaxy.sport.load;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.jsoup.JSoupLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.sport.domain.Player;
import com.lazygalaxy.sport.domain.Team;

public class PlayerWhoScoredJSoupLoad extends JSoupLoad<Player> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private final MongoHelper<Country> countryHelper = MongoHelper.getHelper(Country.class);
	private final MongoHelper<Team> teamHelper = MongoHelper.getHelper(Team.class);

	public PlayerWhoScoredJSoupLoad() throws Exception {
		super(Player.class);
	}

	@Override
	public List<Player> getMongoDocuments(Document htmlDocument) throws Exception {
		Elements playerInfoBlockElements = htmlDocument.select("dl[class=player-info-block]");

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
				country = countryHelper.getDocumentsByLabel(element.select("dd").text()).get(0);
			} else if (StringUtils.startsWith(element.select("dt").text(), "Height")) {
				height = Integer.parseInt(GeneralUtil.numerify(element.select("dd").text()));
			} else if (StringUtils.startsWith(element.select("dt").text(), "Weight")) {
				weight = Integer.parseInt(GeneralUtil.numerify(element.select("dd").text()));
			} else if (StringUtils.startsWith(element.select("dt").text(), "Current Team")) {
				team = teamHelper.getDocumentsByLabel(element.select("dd").text()).get(0);
			} else if (StringUtils.startsWith(element.select("dt").text(), "Position")) {
				whoScoredPosition = Player.Position.getPosition(element.select("dd").text());
			}
		}

		Element playerPictureElement = htmlDocument.select("img[class=player-picture]").get(0);
		String playerPictureLink = playerPictureElement.attr("src");
		int index = playerPictureLink.lastIndexOf('/');
		whoScoredId = Integer.parseInt(playerPictureLink.substring(index + 1, playerPictureLink.length() - 4));

		return Arrays
				.asList(new Player(birthDate, name, country, whoScoredId, height, weight, team, whoScoredPosition));
	}
}