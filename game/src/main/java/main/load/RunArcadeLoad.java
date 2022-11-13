package main.load;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import main.load.arcade.A1_RunArcadeLazyGalaxyGameListLoad;
import main.load.arcade.A2_RunArcadeRickDangerousUltimateGameListLoad;
import main.load.arcade.A4_RunArcadeCoinOpsRomLoad;
import main.load.arcade.B1_RunArcadeMameGameInfoEnrichLoad;
import main.load.common.C1_RunCommonDeriveEnrichLoad;

public class RunArcadeLoad {
    public static void main(String[] args) throws Exception {
        String[] newArgs = new String[]{"SOMETHING"};
        try {
            A1_RunArcadeLazyGalaxyGameListLoad.main(newArgs);
            A2_RunArcadeRickDangerousUltimateGameListLoad.main(newArgs);
            //A3_RunArcadeWolfanoz2kGameListLoad.main(newArgs);
            A4_RunArcadeCoinOpsRomLoad.main(newArgs);
            B1_RunArcadeMameGameInfoEnrichLoad.main(newArgs);
            //B2_RunArcadeCategoryEnrichLoad.main(newArgs);
            C1_RunCommonDeriveEnrichLoad.main(newArgs);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
