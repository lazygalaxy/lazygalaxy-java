package main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.GameListEnrichLoad;

public class XX_RunWolfanoz2kSourceLoad extends GameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.WOLFANOZ_12K, false, true);

        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
