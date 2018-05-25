package com.lazygalaxy.sport.domain;

public class Country extends MongoDocument {
	private String iso2;
	private String iso3;

	public Country() {
	}

	public Country(String name, String[] labels, String iso2, String iso3) {
		super(iso2, name, labels);
		this.iso2 = iso2;
		this.iso3 = iso3;
	}

	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((iso2 == null) ? 0 : iso2.hashCode());
		result = prime * result + ((iso3 == null) ? 0 : iso3.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (iso2 == null) {
			if (other.iso2 != null)
				return false;
		} else if (!iso2.equals(other.iso2))
			return false;
		if (iso3 == null) {
			if (other.iso3 != null)
				return false;
		} else if (!iso3.equals(other.iso3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " " + iso2 + " " + iso3;
	}
}
