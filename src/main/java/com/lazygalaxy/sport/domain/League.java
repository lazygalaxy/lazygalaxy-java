package com.lazygalaxy.sport.domain;

public class League extends MongoDocument {
	private String countryId;
	private String liveScoreCode;
	private String yahooCode;
	private String whoScoredCode;

	public League() {
	}

	public League(String id, String name, String[] labels, Country country, String liveScoreCode, String yahooCode,
			String whoScoredCode) {
		super(id, name, labels);
		this.countryId = country.getId();
		this.liveScoreCode = liveScoreCode;
		this.yahooCode = yahooCode;
		this.whoScoredCode = whoScoredCode;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getLiveScoreCode() {
		return liveScoreCode;
	}

	public void setLiveScoreCode(String liveScoreCode) {
		this.liveScoreCode = liveScoreCode;
	}

	public String getYahooCode() {
		return yahooCode;
	}

	public void setYahooCode(String yahooCode) {
		this.yahooCode = yahooCode;
	}

	public String getWhoScoredCode() {
		return whoScoredCode;
	}

	public void setWhoScoredCode(String whoScoredCode) {
		this.whoScoredCode = whoScoredCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((liveScoreCode == null) ? 0 : liveScoreCode.hashCode());
		result = prime * result + ((yahooCode == null) ? 0 : yahooCode.hashCode());
		result = prime * result + ((whoScoredCode == null) ? 0 : whoScoredCode.hashCode());

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
		if (yahooCode == null) {
			if (other.yahooCode != null)
				return false;
		} else if (!yahooCode.equals(other.yahooCode))
			return false;
		if (whoScoredCode == null) {
			if (other.whoScoredCode != null)
				return false;
		} else if (!whoScoredCode.equals(other.whoScoredCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " " + countryId + " " + liveScoreCode + " " + yahooCode + " " + whoScoredCode;
	}
}
