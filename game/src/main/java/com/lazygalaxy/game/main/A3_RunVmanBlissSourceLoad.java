package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;

public class A3_RunVmanBlissSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad(GameSource.VMAN_BLISS);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
