package com.lazygalaxy.domain.sport;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.domain.world.Country;
import com.lazygalaxy.utils.GeneralUtil;

public class Team extends MongoDocument {
	public class WhoScored {
		public Integer id;

		public WhoScored() {
		}
	}

	public class FantasyPremierLeague {
		public Integer code;

		public FantasyPremierLeague() {
		}
	}

	public String countryId;
	public String shortName;
	public WhoScored whoScored = new WhoScored();
	public FantasyPremierLeague fantasyPremierLeague = new FantasyPremierLeague();

	public Team() {
	}

	public Team(String name, String[] labels, Country country) throws Exception {
		super(buildId("", country.id, GeneralUtil.alphanumerify(name)), name, labels);
		this.countryId = country.id;
	}
}
