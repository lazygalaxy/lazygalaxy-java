package com.lazygalaxy.game;

import java.util.Set;

import com.google.common.collect.Sets;

public class Constant {
	public static class Control {
		public static String JOYSTCK = "joystick";
		public static String LIGHTGUN = "lightgun";
		public static String GAMBLING = "gambling";

		public static Set<String> EXCLUDE = Sets.newHashSet(GAMBLING);

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
		public static String BASKETBALL = "basketball";
		public static String LIGHTGUN = "lightgun";
		public static String SPORT = "sport";
	}

	public static class GameSystem {
		public static String ARCADE = "arcade";
		public static String ATOMISWAVE = "atomiswave";
		public static String DAPHNE = "daphne";
		public static String NAOMI = "naomi";
		public static String NEOGEO = "neogeo";
		public static String NEOGEOCD = "neogeocd";
		public static String OPENBOR = "openbor";

		public static Set<String> MAME = Sets.newHashSet(ARCADE, ATOMISWAVE, DAPHNE, NAOMI, NEOGEO);
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

		public final static Set<String> ALL = Sets.newHashSet(LAZYGALAXY, MAME, RICKDANGEROUS_ULTIMATE, WOLFANOZ_12K,
				VMAN_BLISS);
	}
}
