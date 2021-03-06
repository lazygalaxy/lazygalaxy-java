package com.lazygalaxy.game.util;

import junit.framework.TestCase;

public class GameUtilTest extends TestCase {
	public void testPretify() throws Exception {

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons &amp; Dragons : Shadow over Mystara"));

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara"));

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara (USA)"));

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons    :           Shadow over Mystara"));

		assertEquals("Street Fighter II: Hyper Fighting", GameUtil.pretify("Street Fighter II' - Hyper Fighting"));

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara     [ USA]"));
	}
}
