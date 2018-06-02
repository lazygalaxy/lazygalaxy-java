package com.lazygalaxy.sport.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class Player extends MongoDocument {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public enum Position {
		GK, D, M, NA, FW;

		public static Position getPosition(String position) {
			if (StringUtils.contains(GeneralUtil.alphanumerify(position), "forward")) {
				return FW;
			}
			if (StringUtils.contains(GeneralUtil.alphanumerify(position), "midfielder")) {
				return M;
			}
			if (StringUtils.contains(GeneralUtil.alphanumerify(position), "defender")) {
				return D;
			}
			if (StringUtils.contains(GeneralUtil.alphanumerify(position), "goalkeeper")) {
				return GK;
			}
			return NA;
		}
	}

	public LocalDate birthDate;
	public String countryId;
	public Integer whoScoredId;
	public Integer height;
	public Integer weight;
	public String teamId;
	public Position whoScoredPosition;

	public Player() {
	}

	public Player(LocalDate birthDate, String name, Country country, Integer whoScoredId, Integer height,
			Integer weight, Team team, Position whoScoredPosition) throws Exception {

		super(buildId("", birthDate.format(DATE_TIME_FORMATTER), GeneralUtil.alphanumerify(name)), name,
				new String[] {});
		// try {
		this.birthDate = birthDate;
		this.countryId = country.id;
		this.whoScoredId = whoScoredId;
		this.height = height;
		this.weight = weight;
		this.teamId = team.id;
		this.whoScoredPosition = whoScoredPosition;
		String[] nameParts = name.split(" ");
		if (nameParts.length > 1) {
			String label = nameParts[0].charAt(0) + "";
			for (int i = 1; i < nameParts.length; i++) {
				label += nameParts[i];
			}
			this.addLabel(label);
		}
		// } catch (Exception e) {
		// throw new Exception("error creating player: " + name, e);
		// }
	}
}
