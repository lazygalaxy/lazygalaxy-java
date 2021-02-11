package com.lazygalaxy.game.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.game.load.GameXMLLoad;
import com.lazygalaxy.helper.MongoConnectionHelper;

public class RunGameXMLLoad {
	private static final Logger LOGGER = LogManager.getLogger(RunGameXMLLoad.class);

	public static void main(String[] args) throws Exception {
		try {
			new GameXMLLoad().load("xml/mame_game.xml", "game", false);
			LOGGER.info("xml load completed!");
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
