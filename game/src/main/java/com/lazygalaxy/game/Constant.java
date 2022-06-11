package com.lazygalaxy.game;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class Constant {
    public static class Control {
        public final static String JOYSTCK = "joystick";
        public final static String LIGHTGUN = "lightgun";
        public final static String GAMBLING = "gambling";

        public final static List<String> EXCLUDE = Lists.newArrayList(GAMBLING);

        public static boolean isExcluded(Set<String> inputs) {
            if (inputs != null) {
                for (String input : inputs) {
                    if (EXCLUDE.contains(input)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Genre {
        public final static String BASKETBALL = "basketball";
        public final static String LIGHTGUN = "lightgun";
        public final static String SPORT = "sport";
    }

    public static class GameSystem {
        public final static String ARCADE = "arcade";
        public final static String ATOMISWAVE = "atomiswave";
        public final static String DAPHNE = "daphne";
        public final static String MODEL3 = "model3";
        public final static String N64 = "n64";
        public final static String NAOMI = "naomi";
        public final static String NEOGEO = "neogeo";
        public final static String PC = "pc";
        public final static String PSP = "psp";
        public final static String PSX = "psx";
        public final static String SNES = "snes";
        public final static String MEGADRIVE = "megadrive";
        public final static String OPENBOR = "openbor";
        public final static String SCUMMVM = "scummvm";

        public final static List<String> MAME = Lists.newArrayList(ARCADE, ATOMISWAVE, DAPHNE, MODEL3, NAOMI, NEOGEO);
        public final static List<String> CONSOLE = Lists.newArrayList(N64, PSP, PSX, SNES, MEGADRIVE);
        public final static List<String> COMPUTER = Lists.newArrayList(PC, SCUMMVM);

    }

    public static class GameEmulator {
        public final static String MAME2003 = "mame2003";
        public final static String MAME2010 = "mame2010";
        public final static String FBNEO = "fbneo";
    }

    public static class GameSource {
        public final static String LAZYGALAXY = "lazygalaxy";
        public final static String MAME = "mame";
        public final static String RICKDANGEROUS_ULTIMATE = "rickdangerous_ultimate";
        public final static String WOLFANOZ_12K = "wolfanoz_12k";
        public final static String VMAN_BLISS = "vman_bliss";

        public final static String COINOPS = "coinops";

        public final static String SCUMMVM = "scummvm";

        public final static List<String> ALL = Lists.newArrayList(LAZYGALAXY, MAME, RICKDANGEROUS_ULTIMATE,
                WOLFANOZ_12K, VMAN_BLISS);
    }

    public static class CoinOpsVersion {
        public final static String PLAYER_LEGENDS_2 = "playerlegends2";
        public final static String RETRO_ARCADE_2_ELITES = "retroarcade2elites";

        public final static String PI4_LEGENDS_V3 = "pi4legendsv3";

        public final static String OTHER = "other";

        public final static List<String> ALL = Lists.newArrayList(PI4_LEGENDS_V3, PLAYER_LEGENDS_2, RETRO_ARCADE_2_ELITES, OTHER);
    }
}
