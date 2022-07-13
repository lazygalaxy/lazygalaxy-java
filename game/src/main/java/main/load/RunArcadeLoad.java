package main.load;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import main.load.arcade.*;
import main.load.common.C1_RunAllDeriveEnrichLoad;

public class RunArcadeLoad {
    public static void main(String[] args) throws Exception {
        String[] newArgs = new String[]{"SOMETHING"};
        try {
            A1_RunArcadeLazyGalaxyGameListLoad.main(newArgs);
            A2_RunArcadeRickDangerousUltimateGameListLoad.main(newArgs);
            A3_RunArcadeWolfanoz2kGameListLoad.main(newArgs);
            A4_RunArcadeCoinOpsRomLoad.main(newArgs);
            B1_RunArcadeMameGameInfoEnrichLoad.main(newArgs);
            B2_RunArcadeCategoryEnrichLoad.main(newArgs);
            C1_RunAllDeriveEnrichLoad.main(newArgs);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
