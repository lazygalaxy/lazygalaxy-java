package main.load;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import main.load.common.C1_RunAllDeriveEnrichLoad;
import main.load.other.A1_RunOtherLazyGalaxyGameListLoad;
import main.load.other.A2_RunOtherRickDangerousUltimateGameListLoad;
import main.load.other.A3_RunOtherCoinOpsRomLoad;

public class RunOtherLoad {
    public static void main(String[] args) throws Exception {
        String[] newArgs = new String[]{"SOMETHING"};
        try {
            A1_RunOtherLazyGalaxyGameListLoad.main(newArgs);
            A2_RunOtherRickDangerousUltimateGameListLoad.main(newArgs);
            A3_RunOtherCoinOpsRomLoad.main(newArgs);
            C1_RunAllDeriveEnrichLoad.main(newArgs);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
