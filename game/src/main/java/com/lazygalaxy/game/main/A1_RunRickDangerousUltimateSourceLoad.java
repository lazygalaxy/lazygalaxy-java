package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;

public class A1_RunRickDangerousUltimateSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad(GameSource.RICKDANGEROUS_ULTIMATE);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
