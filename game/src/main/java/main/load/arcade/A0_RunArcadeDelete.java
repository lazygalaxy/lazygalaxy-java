package main.load.arcade;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.game.Constant;
import com.lazygalaxy.game.domain.Game;
import com.mongodb.client.model.Filters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class A0_RunArcadeDelete {
    private static final Logger LOGGER = LogManager.getLogger(A0_RunArcadeDelete.class);

    public static void main(String[] args) throws Exception {
        MongoHelper.getHelper(Game.class).deleteDocumentByFilters(Filters.eq("systemId", Constant.GameSystem.ARCADE));
        LOGGER.info("deletion complete!");
    }
}
