package com.lazygalaxy.common.main;

import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.common.util.SetUtil;
import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.load.CSVLoad;
import com.lazygalaxy.engine.util.GeneralUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class RunCountryInfoCSVLoad {
    private static final Logger LOGGER = LogManager.getLogger(RunCountryInfoCSVLoad.class);

    public static void main(String[] args) throws Exception {
        try {
            new CountryCSVLoad().load("csv/country_info.tsv", 1, "\t", null);
            LOGGER.info("csv load completed!");
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }

    private static class CountryCSVLoad extends CSVLoad<Country> {

        public CountryCSVLoad() throws Exception {
            super(Country.class);
        }

        @Override
        protected List<Country> getMongoDocument(String[] tokens) throws Exception {
            String iso2 = tokens[0].toLowerCase();
            Country country = MongoHelper.getHelper(Country.class).getDocumentById(iso2);
            if (country != null) {

                country.include = GeneralUtil.getBoolean(tokens[1]);
                country.labels.add(tokens[2].toLowerCase());
                country.nationality = GeneralUtil.getString(tokens[3]);
                country.capital = GeneralUtil.getString(tokens[4]);
                country.continent = GeneralUtil.getString(tokens[5]);
                country.population = Integer.parseInt(tokens[6]);
                country.languages = SetUtil.addValueToExactSet(country.languages, tokens[7].split(","));
                country.conservativeDressCode = GeneralUtil.getBoolean(tokens[8]);
                country.nationalDish = GeneralUtil.getString(tokens[9]);
                country.topDishes = SetUtil.addValueToExactSet(country.topDishes, tokens[10].split(","));
                country.flagColors = SetUtil.addValueToExactSet(country.flagColors, tokens[11].split(","));
                country.topAttractions = SetUtil.addValueToExactSet(country.topAttractions, tokens[12].split(","));
                if (tokens.length >= 14)
                    country.sportPeople = SetUtil.addValueToExactSet(country.sportPeople, tokens[13].split(","));
                if (tokens.length >= 15)
                    country.fighterPeople = SetUtil.addValueToExactSet(country.fighterPeople, tokens[14].split(","));
                if (tokens.length >= 16)
                    country.nationalAnimal = GeneralUtil.getString(tokens[15]);

                return Arrays.asList(country);
            }
            LOGGER.error(iso2 + " not found");
            return null;
        }

    }
}
