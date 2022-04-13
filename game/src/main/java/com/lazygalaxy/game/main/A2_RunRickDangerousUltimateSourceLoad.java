package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.main.helpers.SourceLoad;

public class A2_RunRickDangerousUltimateSourceLoad extends SourceLoad {

    public static void main(String[] args) throws Exception {
        try {

            gameListEnrichLoad(GameSource.RICKDANGEROUS_ULTIMATE);

        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
