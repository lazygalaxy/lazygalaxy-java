package com.lazygalaxy.game.util;

import com.lazygalaxy.game.domain.GameInfo;

import junit.framework.TestCase;

public class GameUtilTest extends TestCase {
	public void testPretify() throws Exception {

		GameInfo gameInfo = new GameInfo();

		gameInfo.originalName = "Dungeons &amp; Dragons : Shadow over Mystara";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertNull(gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA)";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA", gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara [USA]";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA", gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara [USA](ver2.0)";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA ver2.0", gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA)[ver2.0)]";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA ver2.0", gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA) updated [ver2.0)]";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA updated ver2.0", gameInfo.version);

		gameInfo.originalName = "Dungeons & Dragons    :           Shadow over Mystara";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertNull(null);

		gameInfo.originalName = "Street Fighter II' - Hyper Fighting";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Street Fighter II': Hyper Fighting", gameInfo.name);
		assertNull(null);

		gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara     [ USA]";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dungeons & Dragons: Shadow over Mystara", gameInfo.name);
		assertEquals("USA", gameInfo.version);

		gameInfo.originalName = "Pong (Rev E) external [TTL]";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Pong", gameInfo.name);
		assertEquals("Rev E external TTL", gameInfo.version);

		gameInfo.originalName = "Pong [Rev E] external (TTL)";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Pong", gameInfo.name);
		assertEquals("Rev E external TTL", gameInfo.version);

		gameInfo.originalName = "Dragon's Lair II: Time Warp";
		GameUtil.pretifyName(gameInfo);
		assertEquals("Dragon's Lair 2: Time Warp", gameInfo.name);
		assertNull(gameInfo.version);
	}
}
