package com.lazygalaxy.game.util;

import com.google.common.collect.Lists;
import com.lazygalaxy.game.domain.GameInfo;
import junit.framework.TestCase;

public class GameUtilTest extends TestCase {
    public void testPretify() throws Exception {

        GameInfo gameInfo = new GameInfo();
        gameInfo.year = "1981";

        gameInfo.originalName = "The Revenge of the Nerds - This is it";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Revenge of the Nerds, The: This is it", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Revenge of the Nerds, The", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("Revenge of the Nerds: This is it", Lists.newArrayList(gameInfo.names).get(2));
        assertEquals("Revenge of the Nerds", Lists.newArrayList(gameInfo.names).get(3));

        gameInfo.originalName = "Revenge of the Nerds, The - This is it";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Revenge of the Nerds, The: This is it", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Revenge of the Nerds, The", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("Revenge of the Nerds: This is it", Lists.newArrayList(gameInfo.names).get(2));
        assertEquals("Revenge of the Nerds", Lists.newArrayList(gameInfo.names).get(3));

        gameInfo.originalName = "The Revenge of the Nerds";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Revenge of the Nerds, The", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Revenge of the Nerds", Lists.newArrayList(gameInfo.names).get(1));

        gameInfo.originalName = "Revenge of the Nerds, The";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Revenge of the Nerds, The", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Revenge of the Nerds", Lists.newArrayList(gameInfo.names).get(1));

        gameInfo.originalName = "Dungeons &amp; Dragons : Shadow over Mystara";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("USA", gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara [USA]";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara [USA]", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara [USA](ver2.0)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara [USA]", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("ver2.0", gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA)[ver2.0]";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("USA[ver2.0]", gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara (USA) updated [ver2.0]";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("USA updated [ver2.0]", gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons    :           Shadow over Mystara";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Street Fighter II' - Hyper Fighting";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Street Fighter 2: Hyper Fighting", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Street Fighter 2", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Dungeons & Dragons -           Shadow over Mystara     [ USA]";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dungeons & Dragons: Shadow over Mystara [ USA]", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dungeons & Dragons", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Pong (Rev E) external [TTL]";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Pong", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Rev E external [TTL]", gameInfo.version);

        gameInfo.originalName = "Pong [Rev E] external (TTL)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Pong [Rev E] external", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("TTL", gameInfo.version);

        gameInfo.originalName = "Dragon's Lair II: Time Warp";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dragon's Lair 2: Time Warp", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dragon's Lair 2", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Dragon's Lair II: Time Warp";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dragon's Lair 2: Time Warp", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Dragon's Lair 2", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Dragon's Lair II";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Dragon's Lair 2", Lists.newArrayList(gameInfo.names).get(0));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Tetris: The: Absolute: The Grand Master 2 Plus";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Tetris The Absolute: The Grand Master 2 Plus", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Tetris The Absolute", Lists.newArrayList(gameInfo.names).get(1));
        assertNull(gameInfo.version);

        gameInfo.originalName = "Samurai Shodown V / Samurai Spirits Zero (NGM-2700, set 1)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Samurai Shodown 5", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Samurai Spirits Zero", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("NGM-2700, set 1", gameInfo.version);

        gameInfo.originalName = "Samurai Spirits Zero / Samurai Shodown V (NGM-2700, set 1)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Samurai Spirits Zero", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Samurai Shodown 5", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("NGM-2700, set 1", gameInfo.version);

        gameInfo.originalName = "Samurai Shodown V: The Way of the Dragon / Samurai Spirits Zero - Ken Sent Me (NGM-2700, set 1)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Samurai Shodown 5: The Way of the Dragon", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Samurai Shodown 5", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("Samurai Spirits Zero: Ken Sent Me", Lists.newArrayList(gameInfo.names).get(2));
        assertEquals("Samurai Spirits Zero", Lists.newArrayList(gameInfo.names).get(3));
        assertEquals("NGM-2700, set 1", gameInfo.version);

        gameInfo.originalName = "Sega Rally Championship - Twin/DX (Revision C)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Sega Rally Championship: Twin/DX", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Sega Rally Championship", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("Revision C", gameInfo.version);

        gameInfo.originalName = "Leisure Suit Larry 2: Goes looking for Love (In Several Wrong Places) (Keep Brackets)";
        GameUtil.pretifyName(gameInfo);
        assertEquals("Leisure Suit Larry 2: Goes looking for Love (In Several Wrong Places)", Lists.newArrayList(gameInfo.names).get(0));
        assertEquals("Leisure Suit Larry 2", Lists.newArrayList(gameInfo.names).get(1));
        assertEquals("Keep Brackets", gameInfo.version);
    }
}
