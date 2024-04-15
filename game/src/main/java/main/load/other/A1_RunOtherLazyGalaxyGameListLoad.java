package main.load.other;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.EmulationStationGameListEnrichLoad;

public class A1_RunOtherLazyGalaxyGameListLoad extends EmulationStationGameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {
            load(GameSource.LAZYGALAXY, true, false, Constant.GameSystem.COMPUTER.toArray(new String[Constant.GameSystem.COMPUTER.size()]));
            load(GameSource.LAZYGALAXY, true, false, Constant.GameSystem.CONSOLE.toArray(new String[Constant.GameSystem.CONSOLE.size()]));
            load(GameSource.LAZYGALAXY, true, false, Constant.GameSystem.HANDHELD.toArray(new String[Constant.GameSystem.HANDHELD.size()]));
        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }
}
