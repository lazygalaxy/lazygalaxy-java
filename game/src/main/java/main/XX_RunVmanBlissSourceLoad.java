package main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import main.helpers.GameListEnrichLoad;

public class XX_RunVmanBlissSourceLoad extends GameListEnrichLoad {

    public static void main(String[] args) throws Exception {
        try {

            load(GameSource.VMAN_BLISS, false, true);

        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
