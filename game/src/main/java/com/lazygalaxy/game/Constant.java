package com.lazygalaxy.game;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class Constant {
    public static class Values {
        public final static String NO = "no";
        public final static String YES = "yes";
        public final static String UNKNOWN = "unknown";
        public final static String NONE = "none";
        public final static String OTHER = "other";
    }

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

        public final static String FIGHTER = "Fighter";

        public final static String MAZE = "Maze";

        public final static String PLATFORM = "Platform";

        public final static String PUZZLE = "Puzzle";
        public final static String DRIVING = "Driving";

        public final static String SHOOTER = "Shooter";

        public final static String SPORTS = "Sports";

        public final static List<String> ALL = Lists.newArrayList(FIGHTER, MAZE, PLATFORM, PUZZLE, DRIVING, SHOOTER, SPORTS);

    }

    public static class SubGenre {

        public final static String ACTION = "Action";
        public final static String ADVENTURE = "Adventure";

        public final static String BALLGUIDE = "Ball Guide";
        public final static String BEATEMUP = "Beat Em Up";

        public final static String BOXING = "Boxing";

        public final static String BREAKOUT = "Breakout";

        public final static String CAR = "Car";

        public final static String COLLECT = "Collect";

        public final static String DRIVING = "Driving";

        public final static String FIELD = "Field";

        public final static String FIGHTER = "Fighter";

        public final static String FLYING = "Flying";

        public final static String GALLERY = "Gallery";

        public final static String GUNNER = "Gunner";

        public final static String MAHJONG = "Mahjong";

        public final static String OUTLINE = "Outline";

        public final static String RUNNGUN = "Run N Gun";
        public final static String SHOOTEMUP = "Shoot Em Up";

        public final static String SHOOTER = "Shooter";

        public final static String VERSUS = "Versus";

        public final static String VERSUSCOOP = "Versus Co-op";

        public final static String WALKING = "Walking";
    }

    public static class CameraGenre {
        public final static String AERIAL = "Aerial";
        public final static String DIAGONAL = "Diagonal";
        public final static String FIRSTPERSON = "1st Person";

        public final static String HORIZONTAL = "Horizontal";

        public final static String SCROLLING = "Scrolling";
        public final static String THIRDPERSON = "3rd Person";

        public final static String VERTICAL = "Vertical";

    }

    public static class GraphcisGenre {
        public final static String TWODIMENSION = "2D";
        public final static String TWOHALFDIMENSION = "2.5D";
        public final static String THREEDIMENSION = "3D";
    }

    public static class GameSystem {
        public final static String THREESDO = "3do";
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
        public final static String MODEL2 = "model2";
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

        public final static String ZXSPECTRUM = "zxspectrum";

        public final static List<String> MAME = Lists.newArrayList(ARCADE, ATOMISWAVE, DAPHNE, FBNEO, MAME2003, MAME2010, MODEL2, MODEL3, NAOMI, NEOGEO, TRIFORCE);
        public final static List<String> CONSOLE = Lists.newArrayList(THREESDO, DREAMCAST, GAMEANDWATCH, GAMECUBE, MEGADRIVE, N64, NES, PCENGINE, PSX, PS2, SEGACD, SFC, SNES);
        public final static List<String> COMPUTER = Lists.newArrayList(C64, PC, ZXSPECTRUM);
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
        public final static String COMPLETE_PLAY = "complete_play";
        public final static String RICKDANGEROUS_ULTIMATE = "rickdangerous_ultimate";
        public final static String WOLFANOZ_12K = "wolfanoz_12k";
        public final static String COINOPS = "coinops";

        //the order determines the importance of the game sources
        public final static List<String> ALL = Lists.newArrayList(LAZYGALAXY, MAME, ARCADE_ITALIA, RICKDANGEROUS_ULTIMATE, COMPLETE_PLAY, WOLFANOZ_12K, COINOPS);
    }

    public static class CoinOpsVersion {
        public final static String FU_ATARASHII = "fuatarashii";
        public final static String FW_ATARASHII_MAX2 = "fwatarashiimax2";
        
        public final static List<String> ALL = Lists.newArrayList(FW_ATARASHII_MAX2, FU_ATARASHII);
    }
}
