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
    public GameInfo(String gameId, String systemId, String originalName, String year, String players, List<String> manufacturers, Set<String> emulatorVersions,
                    Boolean isVertical, Set<String> inputs, String ways, Integer buttons, String status, Boolean isGuess) {
        this(gameId, systemId, null, originalName, year, null, null, null, null, null, null, players, manufacturers, emulatorVersions,
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
        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "atari")) {
            return "Atari";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "capcom")) {
            return "Capcom";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "dataeast")) {
            return "Data East";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "irem")) {
            return "Irem";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "midway")) {
            return "Midway";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nazca")) {
            return "Nazca";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nintendo")) {
            return "Nintendo";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nmk")) {
            return "NMK";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "orientalsoft")) {
            return "Oriental Soft";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "romstar")) {
            return "Romstar";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sega")) {
            return "SEGA";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "snk") || StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "playmore")) {
            return "SNK";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sammy")) {
            return "Sammy";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sternelectronics")) {
            return "Stern Electronics";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "taito")) {
            return "Taito";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "technosjapan")) {
            return "Technos Japan";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "videosystem") ||
                StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "vsystem")) {
            return "Video System";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "virgin")) {
            return "Virgin";
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "inc.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "inc.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "ltd.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "ltd.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "co.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "co.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer), "license")) {
            return normalizeManufacturer(clean(StringUtils.replaceIgnoreCase(manufacturer, "license", "").trim()));
        }

        if (StringUtils.equals(GeneralUtil.alphanumerify(manufacturer), "bootleg") || StringUtils.equals(GeneralUtil.alphanumerify(manufacturer), "hack")) {
            return null;
        }

        return manufacturer;
    }

    private String clean(String value) {
        if (StringUtils.endsWith(value, ",")) {
            return StringUtils.substring(value, 0, value.length() - 1);
        }
        return value;
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
