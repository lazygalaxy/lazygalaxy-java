package main.load.console;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.GameListEnrichLoad;

public class A1_RunConsoleLazyGalaxyGameListLoad extends GameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {
            load(GameSource.LAZYGALAXY, true, false, Constant.GameSystem.CONSOLE.toArray(new String[Constant.GameSystem.CONSOLE.size()]));
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
