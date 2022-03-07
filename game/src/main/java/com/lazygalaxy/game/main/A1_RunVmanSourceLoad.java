package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;

public class A1_RunVmanSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad("vman");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
