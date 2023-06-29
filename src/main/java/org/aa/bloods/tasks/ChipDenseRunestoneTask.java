package org.aa.bloods.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.bloods.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class ChipDenseRunestoneTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {

        boolean c1 = Constants.DENSE_ESSENCE_AREA.contains(Players.local());
        boolean c2 = hasPickaxe();
        boolean c3 = !Inventory.isFull();

//        logger.info(
//                !c1 ? "c1" : !c2 ? "c2" : !c3 ? "c3" : "?????"
//        );

        return c1 && c2 && c3;
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (isAnimating()) {
            logger.info("Waiting to complete animation");
            return;
        }

        logger.info("Attempting to chip dense runestone");
        GameObject denseRunestone = Objects.stream().name("Dense runestone").action("Chip").nearest().first();

        if (!denseRunestone.valid()) {
            logger.info("Unable to locate a valid runestone");
            return;
        }

        boolean chippingRunestone = denseRunestone.interact("Chip") && Condition.wait(this::isAnimating, 350, 15);

        logger.info(chippingRunestone ? "Successfully chipping dense runestone" : "Failed to chip dense runestone");
    }

    private boolean hasPickaxe() {
        return Equipment.stream().nameContains(" pickaxe").isNotEmpty() || Inventory.stream().nameContains(" pickaxe").isNotEmpty();
    }

    private boolean isAnimating() {
        return Players.local().animation() != -1;
    }
}
