package com.lazygalaxy.game.main.load.scummvm;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.Constant.GameSystem;
import com.lazygalaxy.game.main.helpers.SourceLoad;

public class A2_RunScummVMGameListEnrichLoad extends SourceLoad {

    public static void main(String[] args) throws Exception {
        try {

            gameListEnrichLoad(GameSource.RICKDANGEROUS_ULTIMATE, GameSystem.SCUMMVM);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
