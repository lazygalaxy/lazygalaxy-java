package com.lazygalaxy.sport.domain;

import org.bson.types.ObjectId;

public class Team extends MongoDocument {
	private ObjectId countryId;

	public Team() {
	}

	public Team(String name, String[] labels, Country country) {
		super(name, labels);
		this.countryId = country.getId();
	}

	public ObjectId getCountryId() {
		return countryId;
	}

	public void setCountryId(ObjectId countryId) {
		this.countryId = countryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " " + countryId;
	}
}
