package com.lazygalaxy.game.domain;

import org.apache.commons.lang3.StringUtils;

public class GenreInfo implements Comparable<GenreInfo> {
    public String main;
    public String sub;
    public String camera;

    public GenreInfo(String main, String sub, String camera) {
        this.main = main;
        this.sub = sub;
        this.camera = camera;
    }

    @Override
    public int compareTo(GenreInfo o) {
        if (!StringUtils.equals(main, o.main)) {
            return main.compareTo(o.main);
        } else if (!StringUtils.equals(sub, o.sub)) {
            return sub.compareTo(o.sub);
        } else {
            return camera.compareTo(o.camera);
        }
    }
}
