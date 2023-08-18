package com.lazygalaxy.common.domain;

import com.lazygalaxy.engine.domain.MongoDocument;

import java.util.Set;

public class Country extends MongoDocument {
    public static final String DATABASE = "common";

    public String iso2;
    public String iso3;
    public String capital;

    public String continent;
    public int population;

    public Set<String> languages;

    public String nationalDish;

    public Set<String> topDishes;

    public Set<String> flagColors;

    public Set<String> topAttractions;

    public String traditionalFemaleClothes;
    public String tribalTatoo;

    public Country() {
    }

    public Country(String name, String[] labels, String iso2, String iso3) throws Exception {
        super(iso2, name, labels);
        this.iso2 = iso2.toLowerCase();
        this.iso3 = iso3.toLowerCase();
    }
}
