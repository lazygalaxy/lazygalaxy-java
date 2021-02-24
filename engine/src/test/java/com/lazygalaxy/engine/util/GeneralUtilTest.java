package com.lazygalaxy.engine.util;

import junit.framework.TestCase;

public class GeneralUtilTest extends TestCase {
	public void testPretify() throws Exception {

		assertEquals("Dungeons & Dragons: Shadow over Mystara",
				GeneralUtil.pretify("Dungeons &amp; Dragons : Shadow over Mystara"));
	}

}
