package main.reports;

import com.google.common.collect.Lists;
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
                Filters.or(Filters.ne("playerlegends2GameInfo", null), Filters.ne("retroarcade2elitesGameInfo", null), Filters.ne("coinopsotherGameInfo", null)));

        String seperator = "\t";

        Path filePath = Paths
                .get("/Users/vangos/Development/git/lazygalaxy-java/game/target/CoinOps_GameList_v1_2.txt");
        Files.writeString(filePath,
                "System" + seperator + "Name" + seperator + "Year" + seperator + "ROM" + seperator + "Players" + seperator +
                        "Joystick" + seperator + "Buttons" + seperator + "LightGun" + seperator + "TrackBall" + seperator + "Other Input"
                        + seperator + "Vertical" + seperator + "Manufacturers" + seperator + "Player Legends 2" + seperator + "Retro Arcade 2 Elites" + seperator + "Other CoinOps\n",
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        for (Game game : games) {
            Files.writeString(filePath,
                    game.systemId + seperator + game.name + seperator + game.year + seperator + game.gameId + seperator
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
                            + seperator + (game.isVeritcal != null && game.isVeritcal ? "Yes" : "No") + seperator
                            + (StringUtils.equals(game.developer, game.publisher) ? game.developer
                            : game.developer + " / " + game.publisher)
                            + seperator + (game.playerlegends2GameInfo != null ? "Yes" : "No") + seperator
                            + (game.retroarcade2elitesGameInfo != null ? "Yes" : "No") + seperator + "Yes" + "\n",
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
