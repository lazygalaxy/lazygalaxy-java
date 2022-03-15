package com.lazygalaxy.game;

public class Constant {
	public static class Control {
		public static String JOYSTCK = "joystick";
		public static String LIGHTGUN = "lightgun";
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
	}

	public static class GameEmulator {
		public final static String MAME2003 = "mame2003";
		public final static String MAME2010 = "mame2010";
		public final static String FBNEO = "fbneo";
	}

	public static class GameSource {
		public final static String MAME = "mame";
		public final static String RICKDANGEROUS = "rickdangerous";
		public final static String WOLFANOZ = "wolfanoz";
		public final static String VMAN = "vman";

		public final static String[] ALL = new String[] { MAME, RICKDANGEROUS, WOLFANOZ, VMAN };
	}
}
