package main.load.console;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.GameListEnrichLoad;

public class A2_RunConsoleRickDangerousUltimateGameListLoad extends GameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.RICKDANGEROUS_ULTIMATE, true, false, Constant.GameSystem.CONSOLE.toArray(new String[Constant.GameSystem.CONSOLE.size()]));

        } finally {
            if (args.length == 0) {
                MongoConnectionHelper.INSTANCE.close();
            }
        }
    }
}
