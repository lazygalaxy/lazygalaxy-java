package main.load.scummvm;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import main.helpers.EmulationStationGameListEnrichLoad;

public class A2_RunScummVMGameListLoad extends EmulationStationGameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.RICKDANGEROUS_ULTIMATE, false, false, GameSystem.SCUMMVM);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
