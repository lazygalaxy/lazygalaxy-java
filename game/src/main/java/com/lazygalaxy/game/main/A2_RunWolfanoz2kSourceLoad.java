package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;

public class A2_RunWolfanoz2kSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad(GameSource.WOLFANOZ_12K);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
