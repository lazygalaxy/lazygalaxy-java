package com.lazygalaxy.game.domain;

import org.apache.commons.lang3.StringUtils;

public class GenreInfo implements Comparable<GenreInfo> {
    public String main;
    public String sub;
    public String sub2;
    public String camera;
    public String graphics;

    public GenreInfo(String main, String sub, String sub2, String camera, String graphics) {
        this.main = main;
        this.sub = sub;
        this.sub2 = sub2;
        this.camera = camera;
        this.graphics = graphics;
    }

    @Override
    public int compareTo(GenreInfo o) {
        if (!StringUtils.equals(main, o.main)) {
            return main.compareTo(o.main);
        } else if (!StringUtils.equals(sub, o.sub)) {
            return sub.compareTo(o.sub);
        } else if (!StringUtils.equals(sub2, o.sub2)) {
            return sub2.compareTo(o.sub2);
        } else if (!StringUtils.equals(camera, o.camera)) {
            return camera.compareTo(o.camera);
        } else {
            return graphics.compareTo(o.graphics);
        }
    }

    @Override
    public String toString() {
        return main + " " + sub + " " + sub2 + " " + camera + " " + graphics;
    }
}
