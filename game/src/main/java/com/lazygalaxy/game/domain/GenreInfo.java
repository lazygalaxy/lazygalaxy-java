package com.lazygalaxy.game.domain;

import org.apache.commons.lang3.StringUtils;

public class GenreInfo implements Comparable<GenreInfo> {
    public String main;
    public String sub;

    public GenreInfo(String main, String sub) {
        this.main = main;
        this.sub = sub;
    }

    @Override
    public int compareTo(GenreInfo o) {
        if (!StringUtils.equals(main, o.main)) {
            return main.compareTo(o.main);
        } else {
            return sub.compareTo(o.sub);
        }
    }
}
