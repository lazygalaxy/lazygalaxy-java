package com.lazygalaxy.common.domain;

import com.lazygalaxy.engine.domain.MongoDocument;

import java.util.LinkedHashSet;

public class Country extends MongoDocument {
    public static final String DATABASE = "common";

    public String iso2;
    public String iso3;

    public boolean include;
    public String nationality;
    public String capital;
    public String continent;
    public int population;
    public LinkedHashSet<String> languages;
    public boolean conservativeDressCode;

    public String nationalDish;

    public LinkedHashSet<String> topDishes;

    public LinkedHashSet<String> flagColors;

    public LinkedHashSet<String> topAttractions;

    public LinkedHashSet<String> sportPeople;

    public LinkedHashSet<String> fighterPeople;

    public String nationalAnimal;

    public Country() {
    }

    public Country(String name, String[] labels, String iso2, String iso3) throws Exception {
        super(iso2, name, labels);
        this.iso2 = iso2.toLowerCase();
        this.iso3 = iso3.toLowerCase();
    }
}
