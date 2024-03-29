package main.load.other;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.mongodb.client.model.Filters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class A0_RunOtherDelete {
    private static final Logger LOGGER = LogManager.getLogger(A0_RunOtherDelete.class);

    public static void main(String[] args) throws Exception {
        for (String system :
                Constant.GameSystem.CONSOLE) {
            MongoHelper.getHelper(Game.class).deleteDocumentByFilters(Filters.in("systemId", system));
        }
        for (String system :
                Constant.GameSystem.COMPUTER) {
            MongoHelper.getHelper(Game.class).deleteDocumentByFilters(Filters.in("systemId", system));
        }
        for (String system :
                Constant.GameSystem.HANDHELD) {
            MongoHelper.getHelper(Game.class).deleteDocumentByFilters(Filters.in("systemId", system));
        }

        LOGGER.info("deletion complete!");
    }
}
