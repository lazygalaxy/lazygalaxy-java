package com.lazygalaxy.sport.domain;

import com.lazygalaxy.sport.utils.GeneralUtil;

public class Player extends MongoDocument {
	private static String getName(String firstName, String middleName, String surname) {
		StringBuilder builder = new StringBuilder();
		if (firstName != null) {
			builder.append(firstName).append(" ");
		}
		if (middleName != null) {
			builder.append(middleName).append(" ");
		}
		if (surname != null) {
			builder.append(surname).append(" ");
		}
		return builder.toString().trim();
	}

	public String firstName;
	public String middleName;
	public String surname;
	public String countryId;
	public String position;
	public int whoScoredId;

	public Player() {
	}

	public Player(String firstName, String middleName, String surname, Country country, String position,
			int whoScoredId) {
		super(GeneralUtil.simplify(country.id) + "_" + GeneralUtil.simplify(getName(firstName, middleName, surname)),
				getName(firstName, middleName, surname), new String[] {});
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.countryId = country.id;
		this.position = position;
		this.whoScoredId = whoScoredId;
	}
}
