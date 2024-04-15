package com.lazygalaxy.game.domain;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.GameSystem;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class Game extends MongoDocument {
    public static final String DATABASE = "game";
    private static final String ID_SEPERATOR = ":";

    public String gameId;
    public String systemId;
    public String subSystemId;
    public String version;
    public Long fileSize;

    public Set<String> family;
    public Boolean favourite;
    public Boolean hide;

    public GameInfo lazygalaxyGameInfo;
    public GameInfo mameGameInfo;
    public GameInfo arcadeitaliaGameInfo;
    public GameInfo rickdangerous_ultimateGameInfo;
    public GameInfo complete_playGameInfo;
    public GameInfo wolfanoz_12kGameInfo;
    public GameInfo coinopsGameInfo;
    public GameInfo scummvmGameInfo;
    // retropie images


    public Set<String> coinopsVersions;

    public String year;
    public Integer players;
    public String description;
    public String developer;
    public String publisher;
    public String manufacturer;
    public String genre;
    public String subGenre;
    public String sub2Genre;
    public String camera;
    public String graphics;
    public Boolean isVertical;
    public Set<String> inputs;
    public String ways;
    public Integer buttons;

    public Game() {

    }

    public Game(String systemId, String gameId, String version, Long fileSize) throws Exception {
        super(createId(systemId, gameId, version), false, null, null);
        this.gameId = gameId;
        this.systemId = systemId;
        this.version = version;
        this.fileSize = fileSize;
    }

    public static String createId(String systemId, String gameId, String version) {
        return systemId + ID_SEPERATOR + gameId + normalizeVersion(systemId, version);
    }

    private static String normalizeVersion(String systemId, String version) {
        if (version != null && StringUtils.equals(systemId, GameSystem.SCUMMVM)) {
            String versionId = GeneralUtil.alphanumerify(version);

            versionId = StringUtils.replace(versionId, "windows", "win");
            versionId = StringUtils.replace(versionId, "floppy", "flo");
            versionId = StringUtils.replace(versionId, "atarist", "ata");
            versionId = StringUtils.replace(versionId, "multilanguage", "ml");
            versionId = StringUtils.replace(versionId, "remake", "re");
            versionId = StringUtils.replace(versionId, "fanmadegetsdetectedasdott", "fan");
            versionId = StringUtils.replace(versionId, "extracted", "ex");
            versionId = StringUtils.replace(versionId, "amiga", "ami");
            versionId = StringUtils.replace(versionId, "linux", "lin");
            versionId = StringUtils.replace(versionId, "apple", "app");
            return ID_SEPERATOR + versionId;
        }
        return "";
    }
}
