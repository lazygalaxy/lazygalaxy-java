package com.lazygalaxy.game.domain;

import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.util.SetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;
import java.util.Set;

public class GameInfo {
    // retroarch
    public String gameId;
    public String systemId;
    public String path;
    public String originalName;
    public String year;
    public String description;
    public String genre;
    public String image;
    public String video;
    public String marquee;
    public Double rating;
    public Integer players;
    public List<String> manufacturers;
    public Set<String> emulatorVersions;


    // derived
    public List<String> names;
    public String version;

    // mame
    public Boolean isVertical;
    public Set<String> inputs;
    public String ways;
    public Integer buttons;
    public String status;
    public Boolean isGuess;

    public GameInfo() {

    }

    // retroarch constructor
    public GameInfo(String gameId, String systemId, String path, String originalName, String year, String description,
                    String genre, String image, String video, String marquee, Double rating, String players,
                    List<String> manufacturers, Set<String> emulatorVersions, Set<String> inputs, String ways, Integer buttons) {
        this(gameId, systemId, path, originalName, year, description, genre, image, video, marquee, rating, players,
                manufacturers, emulatorVersions, null, inputs, ways, buttons, null, null);
    }

    // mame constructor
    public GameInfo(String gameId, String originalName, String year, String players, List<String> manufacturers, Set<String> emulatorVersions,
                    Boolean isVertical, Set<String> inputs, String ways, Integer buttons, String status, Boolean isGuess) {
        this(gameId, null, null, originalName, year, null, null, null, null, null, null, players, manufacturers, emulatorVersions,
                isVertical, inputs, ways, buttons, status, isGuess);
    }

    //coinops constructor
    public GameInfo(String gameId, String systemId) {
        this(gameId, systemId, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
    }

    // scummvm consrtuctor
    public GameInfo(String gameId, String path, String originalName) {
        this(gameId, null, path, originalName, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
    }

    // general constructor
    public GameInfo(String gameId, String systemId, String path, String originalName, String year, String description,
                    String genre, String image, String video, String marquee, Double rating, String players,
                    List<String> manufacturers, Set<String> emulatorVersions, Boolean isVertical, Set<String> inputs, String ways,
                    Integer buttons, String status, Boolean isGuess) {
        this.gameId = gameId;
        this.systemId = systemId;
        if (!StringUtils.endsWith(path, GameSource.LAZYGALAXY)) {
            this.path = path;
        }
        this.originalName = originalName;
        if (year != null && !StringUtils.equals(year, "1970")) {
            this.year = StringUtils.left(year, 4);
            if (StringUtils.contains(this.year, "?")) {
                this.year = null;
            }
        }
        this.description = description;
        this.genre = genre;
        this.image = image;
        this.video = video;
        this.marquee = marquee;
        this.rating = rating;
        if (rating != null && rating <= 0) {
            this.rating = null;
        }
        if (!StringUtils.isBlank(players)) {
            String[] playerArray = players.split("-");
            this.players = Integer.parseInt(GeneralUtil.numerify(playerArray[playerArray.length - 1]));
        }

        // normalize manufacturers
        if (manufacturers != null) {
            for (String manufacturer : manufacturers) {
                if (!StringUtils.isBlank(manufacturer)) {
                    this.manufacturers = SetUtil.addValueToArrayList(this.manufacturers,
                            normalizeManufacturer(manufacturer));
                }
            }
        }

        this.emulatorVersions = emulatorVersions;

        if (isVertical != null) {
            this.isVertical = isVertical;
        }

        this.inputs = inputs;
        this.ways = ways;
        this.buttons = buttons;

        this.status = status;

        if (isGuess != null && isGuess) {
            this.isGuess = isGuess;
        }
    }

    private String normalizeManufacturer(String manufacturer) {
        if (StringUtils.startsWith(GeneralUtil.alphanumerify(manufacturer), "capcom")) {
            return "Capcom";
        }

        if (StringUtils.startsWith(GeneralUtil.alphanumerify(manufacturer), "dataeast")) {
            return "Data East";
        }

        if (StringUtils.startsWith(GeneralUtil.alphanumerify(manufacturer), "nintendo")) {
            return "Nintendo";
        }

        if (StringUtils.startsWith(GeneralUtil.alphanumerify(manufacturer), "sega")) {
            return "SEGA";
        }

        if (StringUtils.startsWith(GeneralUtil.alphanumerify(manufacturer), "taito")) {
            return "Taito";
        }

        return manufacturer;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "description");
    }
}
