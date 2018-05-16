package com.lazygalaxy.sport.domain;

import org.bson.types.ObjectId;

public class League extends MongoDocument {
	private ObjectId countryId;
	private String liveScoreCode;

	public League() {
	}

	public League(String name, String[] labels, Country country, String liveScoreCode) {
		super(name, labels);
		this.countryId = country.getId();
		this.liveScoreCode = liveScoreCode;
	}

	public ObjectId getCountryId() {
		return countryId;
	}

	public void setCountryId(ObjectId countryId) {
		this.countryId = countryId;
	}

	public String getLiveScoreCode() {
		return liveScoreCode;
	}

	public void setLiveScoreCode(String liveScoreCode) {
		this.liveScoreCode = liveScoreCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((liveScoreCode == null) ? 0 : liveScoreCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		League other = (League) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (liveScoreCode == null) {
			if (other.liveScoreCode != null)
				return false;
		} else if (!liveScoreCode.equals(other.liveScoreCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " " + countryId + " " + liveScoreCode;
	}
}
