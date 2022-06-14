package main.load;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import main.load.arcade.A1_RunArcadeLazyGalaxyGameListLoad;
import main.load.arcade.A2_RunArcadeRickDangerousUltimateGameListLoad;
import main.load.arcade.A3_RunArcadeCoinOpsRomLoad;
import main.load.arcade.B1_RunArcadeMameGameInfoEnrichLoad;
import main.load.common.C1_RunAllDeriveEnrichLoad;
import main.load.other.A1_RunOtherLazyGalaxyGameListLoad;
import main.load.other.A2_RunOtherRickDangerousUltimateGameListLoad;
import main.load.other.A3_RunOtherCoinOpsRomLoad;

public class RunCompleteLoad {
    public static void main(String[] args) throws Exception {
        String[] newArgs = new String[]{"SOMETHING"};
        try {
            A1_RunArcadeLazyGalaxyGameListLoad.main(newArgs);
            A1_RunOtherLazyGalaxyGameListLoad.main(newArgs);

            A2_RunArcadeRickDangerousUltimateGameListLoad.main(newArgs);
            A2_RunOtherRickDangerousUltimateGameListLoad.main(newArgs);

            A3_RunArcadeCoinOpsRomLoad.main(newArgs);
            A3_RunOtherCoinOpsRomLoad.main(newArgs);

            B1_RunArcadeMameGameInfoEnrichLoad.main(newArgs);

            C1_RunAllDeriveEnrichLoad.main(newArgs);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
