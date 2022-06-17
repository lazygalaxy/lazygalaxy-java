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
        public final static String C64 = "c64";
        public final static String DAPHNE = "daphne";
        public final static String DREAMCAST = "dreamcast";
        public final static String GBA = "gba";
        public final static String MODEL3 = "model3";
        public final static String N64 = "n64";
        public final static String NAOMI = "naomi";
        public final static String NEOGEO = "neogeo";
        public final static String NES = "nes";
        public final static String TRIFORCE = "triforce";
        public final static String PC = "pc";
        public final static String PCENGINE = "pcengine";
        public final static String PSP = "psp";
        public final static String PSX = "psx";
        public final static String SNES = "snes";
        public final static String MEGADRIVE = "megadrive";
        public final static String OPENBOR = "openbor";
        public final static String SCUMMVM = "scummvm";

        public final static List<String> MAME = Lists.newArrayList(ARCADE, ATOMISWAVE, DAPHNE, MODEL3, NAOMI, NEOGEO, TRIFORCE);
        public final static List<String> CONSOLE = Lists.newArrayList(DREAMCAST, MEGADRIVE, N64, NES, PCENGINE, PSX, SNES);

        public final static List<String> COMPUTER = Lists.newArrayList(C64, PC, SCUMMVM);

        public final static List<String> HANDHELD = Lists.newArrayList(GBA, PSP);

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
        public final static String COLLECTIONS_ARCADE = "collectionsarcade";
        public final static String COLLECTIONS_LEGENDS = "collectionslegends";
        public final static String PLAYER_LEGENDS_2 = "playerlegends2";
        public final static String RETRO_ARCADE_2_ELITES = "retroarcade2elites";

        public final static String PI4_LEGENDS_V3 = "pi4legendsv3";

        public final static List<String> ALL = Lists.newArrayList(COLLECTIONS_ARCADE, COLLECTIONS_LEGENDS, PI4_LEGENDS_V3, PLAYER_LEGENDS_2, RETRO_ARCADE_2_ELITES);
    }
}
