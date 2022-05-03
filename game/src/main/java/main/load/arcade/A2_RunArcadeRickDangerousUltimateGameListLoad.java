package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.GameListEnrichLoad;

public class A2_RunArcadeRickDangerousUltimateGameListLoad extends GameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.RICKDANGEROUS_ULTIMATE, true, true, Constant.GameSystem.MAME.toArray(new String[Constant.GameSystem.MAME.size()]));

        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
