package com.lazygalaxy.game.util;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

public class GameUtilTest extends TestCase {
	public void testPretify() throws Exception {
		Set<String> extraInfo = new TreeSet<String>();

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons &amp; Dragons : Shadow over Mystara"));

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara"));

		extraInfo.clear();
		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara (USA)", extraInfo));
		assertEquals("(USA)", extraInfo.toArray()[0]);

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons    :           Shadow over Mystara"));

		assertEquals("Street Fighter II': Hyper Fighting", GameUtil.pretify("Street Fighter II' - Hyper Fighting"));

		extraInfo.clear();
		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GameUtil.pretify("Dungeons & Dragons -           Shadow over Mystara     [ USA]", extraInfo));
		assertEquals("[ USA]", extraInfo.toArray()[0]);

		extraInfo.clear();
		assertEquals("Pong", GameUtil.pretify("Pong (Rev E) external [TTL]", extraInfo));
		assertEquals("(Rev E) external [TTL]", extraInfo.toArray()[0]);

		extraInfo.clear();
		assertEquals("Pong", GameUtil.pretify("Pong [Rev E] external (TTL)", extraInfo));
		assertEquals("(TTL)", extraInfo.toArray()[0]);
		assertEquals("[Rev E] external", extraInfo.toArray()[1]);
	}
}
