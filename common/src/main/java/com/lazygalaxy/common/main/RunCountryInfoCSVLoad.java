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
            new CountryCSVLoad().load("csv/country_info.csv", 1, ";", null);
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
            Country game = MongoHelper.getHelper(Country.class).getDocumentById(iso2);
            if (game != null) {

                game.labels.add(tokens[1].toLowerCase());
                game.capital = GeneralUtil.getString(tokens[2]);
                game.continent = GeneralUtil.getString(tokens[3]);
                game.population = Integer.parseInt(tokens[4]);
                game.languages = SetUtil.addValueToTreeSet(game.languages, tokens[5].split(","));
                game.nationalDish = GeneralUtil.getString(tokens[6]);
                game.topDishes = SetUtil.addValueToTreeSet(game.topDishes, tokens[7].split(","));
                game.flagColors = SetUtil.addValueToTreeSet(game.flagColors, tokens[8].split(","));
                game.topAttractions = SetUtil.addValueToTreeSet(game.topAttractions, tokens[9].split(","));

                return Arrays.asList(game);
            }
            LOGGER.error(iso2 + " not found");
            return null;
        }

    }
}
