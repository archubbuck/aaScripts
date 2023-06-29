package org.aa.bloods.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.bloods.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;

import java.util.logging.Logger;

public class ActivateBloodEssenceTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && Inventory.stream().id(Constants.BLOOD_ESSENCE_ACTIVE).isEmpty()
                && Inventory.stream().id(Constants.BLOOD_ESSENCE).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to activate blood essence");
        Item bloodEssence = Inventory.stream().id(Constants.BLOOD_ESSENCE).first();
        boolean activatedBloodEssence = bloodEssence.interact("Activate") && Condition.wait(() -> Inventory.stream().id(Constants.BLOOD_ESSENCE_ACTIVE).isNotEmpty(), 350, 15);
        logger.info(activatedBloodEssence ? "Successfully activated blood essence" : "Failed to activate blood essence");
    }
}
