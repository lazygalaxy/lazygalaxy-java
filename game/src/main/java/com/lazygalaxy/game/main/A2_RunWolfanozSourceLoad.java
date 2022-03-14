package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;

public class A2_RunWolfanozSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad("wolfanoz");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
