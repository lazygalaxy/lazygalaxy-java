package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.EmulationStationGameListEnrichLoad;

public class A3_RunArcadeWolfanoz2kGameListLoad extends EmulationStationGameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.WOLFANOZ_12K, true, true, Constant.GameSystem.MAME.toArray(new String[Constant.GameSystem.MAME.size()]));

        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }
}
