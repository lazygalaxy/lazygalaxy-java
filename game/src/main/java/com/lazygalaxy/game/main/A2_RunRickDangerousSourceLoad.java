package com.lazygalaxy.game.main;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;

public class A2_RunRickDangerousSourceLoad extends SourceLoad {

	public static void main(String[] args) throws Exception {
		try {

			sourceLoad("rickdangerous");

		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
