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
        public final static String BEATEMUP = "Beat Em Up";
        public final static String FIGHTER = "Fighter";
        public final static String MAZE = "Maze";
        public final static String OTHER = "Other";
        public final static String PLATFORM = "Platform";
        public final static String PUZZLE = "Puzzle";
        public final static String RACING = "Racing";
        public final static String RUNNGUN = "Run N Gun";
        public final static String SHOOTEMUP = "Shoot Em Up";
        public final static String SPORTS = "Sports";

        public final static List<String> ALL = Lists.newArrayList(BEATEMUP, FIGHTER, PUZZLE, RACING, RUNNGUN, SPORTS);

    }

    public static class SubGenre {
        public final static String AIRCRAFT = "Aircraft";
        public final static String ARMY = "Army";
        public final static String ASIAN = "Asian";
        public final static String CAR = "Car";
        public final static String KINGOFFIGHTERS = "King of Fighters";

        public final static String ROBOT = "Robot";
        public final static String SPACECRAFT = "Spacecraft";
        public final static String STREETFIGHTER = "Street Fighter";

        public final static String TANK = "Tank";
        public final static String TETRIS = "Tetris";
        public final static String TRACKANDFIELD = "Track & Field";
        public final static String OTHER = "Other";
        public final static String UNKNOWN = "Unknown";
    }

    public static class GameSystem {
        public final static String ARCADE = "arcade";
        public final static String ATOMISWAVE = "atomiswave";
        public final static String C64 = "c64";
        public final static String DAPHNE = "daphne";
        public final static String DREAMCAST = "dreamcast";

        public final static String FBNEO = "fbneo";
        public final static String GAMEANDWATCH = "gameandwatch";
        public final static String GAMECUBE = "gc";
        public final static String GBA = "gba";

        public final static String MAME2003 = "mame2003";
        public final static String MAME2010 = "mame2010";
        public final static String MODEL3 = "model3";
        public final static String N64 = "n64";
        public final static String NAOMI = "naomi";
        public final static String NEOGEO = "neogeo";
        public final static String NES = "nes";
        public final static String TRIFORCE = "triforce";
        public final static String PC = "pc";
        public final static String PCENGINE = "pcengine";
        public final static String PS2 = "ps2";
        public final static String PSP = "psp";
        public final static String PSX = "psx";
        public final static String SEGACD = "segacd";
        public final static String SNES = "snes";
        public final static String MEGADRIVE = "megadrive";
        public final static String OPENBOR = "openbor";
        public final static String SFC = "sfc";
        public final static String SCUMMVM = "scummvm";

        public final static List<String> MAME = Lists.newArrayList(ARCADE, ATOMISWAVE, DAPHNE, FBNEO, MAME2003, MAME2010, MODEL3, NAOMI, NEOGEO, TRIFORCE);
        public final static List<String> CONSOLE = Lists.newArrayList(DREAMCAST, GAMECUBE, MEGADRIVE, N64, NES, PCENGINE, PSX, PS2, SEGACD, SFC, SNES);

        public final static List<String> COMPUTER = Lists.newArrayList(C64, PC);

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
        public final static String ARCADE_ITALIA = "arcadeitalia";
        public final static String RICKDANGEROUS_ULTIMATE = "rickdangerous_ultimate";
        public final static String WOLFANOZ_12K = "wolfanoz_12k";
        public final static String COINOPS = "coinops";

        //the order determines the importance of the game sources
        public final static List<String> ALL = Lists.newArrayList(LAZYGALAXY, MAME, ARCADE_ITALIA, RICKDANGEROUS_ULTIMATE, WOLFANOZ_12K, COINOPS);
    }

    public static class CoinOpsVersion {
        public final static String FORGOTTEN_WORLDS_2 = "forgottenworlds2";
        public final static String LEGENDS_3_DESK = "legends3deck";

        public final static String LINUX_LEGENDS_3_V1 = "linuxlegends3v1";
        public final static String PLAYER_LEGENDS_4_MAX = "playerlegends4max";
        public final static String PLAYER_LEGENDS_4 = "playerlegends4";
        public final static String COLLECTIONS_ARCADE = "collectionsarcade";
        public final static String COLLECTIONS_LEGENDS = "collectionslegends";
        public final static String PI4_LEGENDS_2_V3 = "pi4legends2v3";

        public final static List<String> ALL = Lists.newArrayList(FORGOTTEN_WORLDS_2, LEGENDS_3_DESK, LINUX_LEGENDS_3_V1, PLAYER_LEGENDS_4_MAX, PLAYER_LEGENDS_4, COLLECTIONS_ARCADE, COLLECTIONS_LEGENDS, PI4_LEGENDS_2_V3);
    }
}
