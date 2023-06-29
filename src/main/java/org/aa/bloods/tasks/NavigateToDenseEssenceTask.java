package org.aa.bloods.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.bloods.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class NavigateToDenseEssenceTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {

        boolean c1 = !Constants.DENSE_ESSENCE_AREA.contains(Players.local());
        boolean c2 = !Inventory.isFull();

//        logger.info(
//                !c1 ? "c1" : !c2 ? "c2" : "?????"
//        );

        return c1 && c2;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to the dense essence area");

        Movement.builder(Constants.DENSE_ESSENCE_AREA.getCentralTile())
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .move();

        boolean navigating = Condition.wait(() -> Players.local().inMotion(), 150, 15);
        logger.info(navigating ? "Successfully navigating to the dense essence area" : "Failed to start navigating to the dense essence area");
    }
}
