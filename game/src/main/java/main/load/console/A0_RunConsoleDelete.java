package main.load.console;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.mongodb.client.model.Filters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class A0_RunConsoleDelete {
    private static final Logger LOGGER = LogManager.getLogger(A0_RunConsoleDelete.class);

    public static void main(String[] args) throws Exception {
        MongoHelper.getHelper(Game.class).deleteDocumentByFilters(Filters.in("systemId", Constant.GameSystem.CONSOLE));
        LOGGER.info("deletion complete!");
    }
}
