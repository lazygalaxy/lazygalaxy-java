package main.report;

import com.google.common.collect.Lists;
import com.lazygalaxy.common.util.SetUtil;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.GameUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenerateCoinOpsGameListFromDB {
    private static final Logger LOGGER = LogManager.getLogger(GenerateCoinOpsGameListFromDB.class);

    public static void main(String[] args) throws Exception {
        List<Game> games = GameUtil.getGames(false, false, null,
                Sorts.ascending(Lists.newArrayList("systemId", "name")),
                Filters.in("coinopsVersions", Constant.CoinOpsVersion.ALL));

        String seperator = "\t";

        Path filePath = Paths
                .get("/Users/vangos/Development/git/lazygalaxy-java/game/src/main/resources/report/CoinOps_GameList.txt");
        Files.writeString(filePath,
                "System" + seperator +
                        "SubSystem" + seperator +
                        "Name" + seperator +
                        "Year" + seperator +
                        "ROM" + seperator +
                        "Players" + seperator +
                        "Joystick" + seperator +
                        "Ways" + seperator +
                        "Buttons" + seperator +
                        "LightGun" + seperator +
                        "Stick" + seperator +
                        "TrackBall" + seperator +
                        "Other Input" + seperator +
                        "Vertical" + seperator +
                        "Manufacturer" + seperator +
                        "Genre" + seperator +
                        "SubGenre" + seperator +
                        "Sub2Genre" + seperator +
                        "CameraGenre" + seperator +
                        "GraphicsGenre" + seperator +
                        "FUA" + seperator +
                        "Insert Coin" + seperator +
                        "Pi4 Legends 3 v4" + seperator +
                        "Forgotten Worlds 2" + seperator +
                        "Deck Legends 3" + seperator +
                        "Linux Legends 3" + seperator +
                        "Legends 4 Max + U1" + seperator +
                        "Legends 4 + U1" + seperator +
                        "Collections Arcade" + seperator +
                        "Collections Legends" + seperator +
                        "Pi4 Legends 2 v3\n",
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        for (Game game : games) {
            Files.writeString(filePath,
                    game.systemId + seperator +
                            (game.subSystemId != null && !StringUtils.equals(game.subSystemId, game.systemId) ? game.subSystemId : Constant.Values.NONE) + seperator +
                            game.name + seperator +
                            game.year + seperator +
                            (game.coinopsGameInfo != null && game.coinopsGameInfo.originalName != null ? game.coinopsGameInfo.originalName : game.gameId) + seperator
                            + (game.players != null && game.players > 0 ? game.players : Constant.Values.UNKNOWN) + seperator
                            + (game.inputs != null ? (game.inputs.contains("doublejoy") ? "twin" : (game.inputs.contains("joy") ? "single" : Constant.Values.NONE)) : Constant.Values.UNKNOWN) + seperator
                            + (game.inputs != null ? (game.inputs.contains("joy") || game.inputs.contains("doublejoy") ? game.ways : Constant.Values.NONE) : Constant.Values.UNKNOWN) + seperator
                            + (game.buttons != null ? game.buttons : (game.inputs != null ? "0" : Constant.Values.UNKNOWN)) + seperator
                            + (game.inputs != null ? (game.inputs.contains("lightgun") ? Constant.Values.YES : Constant.Values.NO) : Constant.Values.UNKNOWN) + seperator
                            + (game.inputs != null ? (game.inputs.contains("stick") ? Constant.Values.YES : Constant.Values.NO) : Constant.Values.UNKNOWN) + seperator
                            + (game.inputs != null ? (game.inputs.contains("trackball") ? Constant.Values.YES : Constant.Values.NO) : Constant.Values.UNKNOWN) + seperator
                            + (game.inputs != null ? (getOtherInputString(game.inputs, "joy", "doublejoy", "only_buttons", "lightgun", "trackball", "stick")) : Constant.Values.UNKNOWN) + seperator
                            + (game.isVertical != null && game.isVertical ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (game.manufacturer != null ? game.manufacturer : Constant.Values.UNKNOWN) + seperator
                            + (game.genre != null ? game.genre : Constant.Values.UNKNOWN) + seperator
                            + (game.subGenre != null ? game.subGenre : Constant.Values.UNKNOWN) + seperator
                            + (game.sub2Genre != null ? game.sub2Genre : Constant.Values.UNKNOWN) + seperator
                            + (game.camera != null ? game.camera : Constant.Values.UNKNOWN) + seperator
                            + (game.graphics != null ? game.graphics : Constant.Values.UNKNOWN) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.FU_ATARASHII) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.INSERT_COIN) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PI4_LEGENDS_3_V4) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.FORGOTTEN_WORLDS_2) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.LEGENDS_3_DESK) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.LINUX_LEGENDS_3_V1) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PLAYER_LEGENDS_4_MAX) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PLAYER_LEGENDS_4) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.COLLECTIONS_ARCADE) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.COLLECTIONS_LEGENDS) ? Constant.Values.YES : Constant.Values.NO) + seperator
                            + (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PI4_LEGENDS_2_V3) ? Constant.Values.YES : Constant.Values.NO) + "\n",
                    StandardOpenOption.APPEND);
        }
        LOGGER.info("report done");
    }

    private static String getOtherInputString(Set<String> set, String... items) {
        List<String> newList = new ArrayList<String>(set);
        for (String item : items) {
            newList.remove(item);
        }
        if (newList.isEmpty()) {
            return Constant.Values.NONE;
        }

        return StringUtils.join(newList, "/");
    }
}
