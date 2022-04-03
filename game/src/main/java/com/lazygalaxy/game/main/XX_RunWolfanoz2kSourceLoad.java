package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;
import com.lazygalaxy.game.main.helpers.SourceLoad;

public class XX_RunWolfanoz2kSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad(GameSource.WOLFANOZ_12K);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
