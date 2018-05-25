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

	private String firstName;
	private String middleName;
	private String surname;
	private String countryId;
	private String position;
	private int whoScoredId;

	public Player() {
	}

	public Player(String firstName, String middleName, String surname, Country country, String position,
			int whoScoredId) {
		super(GeneralUtil.simplify(country.getId()) + "_"
				+ GeneralUtil.simplify(getName(firstName, middleName, surname)),
				getName(firstName, middleName, surname), new String[] {});
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.countryId = country.getId();
		this.position = position;
		this.whoScoredId = whoScoredId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}
}
