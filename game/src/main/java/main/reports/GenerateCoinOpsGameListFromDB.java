package main.reports;

import com.google.common.collect.Lists;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.util.GameUtil;
import com.lazygalaxy.game.util.SetUtil;
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
                "System" + seperator + "SubSystem" + seperator + "Name" + seperator + "Year" + seperator + "ROM" + seperator + "Players" + seperator +
                        "Joystick" + seperator + "Buttons" + seperator + "LightGun" + seperator + "TrackBall" + seperator + "Other Input" + seperator +
                        "Vertical" + seperator +
                        "Developer" + seperator +
                        "Publisher" + seperator +
                        "Genre" + seperator +
                        "SubGenre" + seperator +
                        "Collections Arcade" + seperator +
                        "Collections Legends" + seperator +
                        "Pi4 Legends v3" + seperator +
                        "Player Legends 2 Max" + seperator +
                        "Player Legends 2" + seperator +
                        "Retro Arcade 2 Elites\n",
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        for (Game game : games) {
            Files.writeString(filePath,
                    game.systemId + seperator + (game.subSystemId != null && !StringUtils.equals(game.subSystemId, game.systemId) ? game.subSystemId : "none") + seperator + game.name + seperator + game.year + seperator + game.gameId + seperator
                            + (game.players != null && game.players > 0 ? game.players : "unknown")
                            + seperator
                            + (game.inputs != null ? (game.inputs.contains("joy") ? "joy" + game.ways + "way" : (game.inputs.contains("doublejoy") ? "doublejoy" + game.ways + "way" : "none")) : "unknown")
                            + seperator
                            + (game.buttons != null ? game.buttons : (game.inputs != null ? "0" : "unknown"))
                            + seperator
                            + (game.inputs != null ? (game.inputs.contains("lightgun") ? "Yes" : "No") : "unknown")
                            + seperator
                            + (game.inputs != null ? (game.inputs.contains("trackball") ? "Yes" : "No") : "unknown")
                            + seperator
                            + (game.inputs != null ? (getOtherInputString(game.inputs, "joy", "doublejoy", "only_buttons", "lightgun", "trackball")) : "unknown")
                            + seperator + (game.isVertical != null && game.isVertical ? "Yes" : "No") + seperator
                            + (game.developer != null ? game.developer : "unknown") + seperator
                            + (game.publisher != null ? game.publisher : "unknown") + seperator
                            + (game.genre != null ? game.genre : "unknown") + seperator
                            + (game.subGenre != null ? game.subGenre : "unknown") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.COLLECTIONS_ARCADE) ? "Yes" : "No") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.COLLECTIONS_LEGENDS) ? "Yes" : "No") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PI4_LEGENDS_V3) ? "Yes" : "No") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PLAYER_LEGENDS_2_MAX) ? "Yes" : "No") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.PLAYER_LEGENDS_2) ? "Yes" : "No") + seperator +
                            (SetUtil.contains(game.coinopsVersions, Constant.CoinOpsVersion.RETRO_ARCADE_2_ELITES) ? "Yes" : "No") + "\n",
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
            return "none";
        }

        return StringUtils.join(newList, "/");
    }
}
