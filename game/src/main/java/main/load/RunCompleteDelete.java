package main.load;

import com.lazygalaxy.engine.helper.MongoConnectionHelper;
import main.load.arcade.A0_RunArcadeDelete;
import main.load.other.A0_RunOtherDelete;

public class RunCompleteDelete {
    public static void main(String[] args) throws Exception {
        String[] newArgs = new String[]{"SOMETHING"};
        try {
            A0_RunArcadeDelete.main(newArgs);
            A0_RunOtherDelete.main(newArgs);
        } finally {
            MongoConnectionHelper.INSTANCE.close();
        }
    }
}
