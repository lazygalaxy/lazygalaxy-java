package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import com.lazygalaxy.game.Constant.GameSource;

public class A0_RunLazyGalaxySourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad(GameSource.LAZYGALAXY);

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
