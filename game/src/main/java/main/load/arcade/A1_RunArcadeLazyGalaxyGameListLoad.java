package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.EmulationStationGameListEnrichLoad;

public class A1_RunArcadeLazyGalaxyGameListLoad extends EmulationStationGameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {
            load(GameSource.LAZYGALAXY, true, true, Constant.GameSystem.MAME.toArray(new String[Constant.GameSystem.MAME.size()]));
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }
}
