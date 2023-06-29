package org.aa.truebloods.tasks;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class ActivateBloodEssence implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Inventory.stream().id(Items.BLOOD_ESSENCE_ACTIVE).isEmpty()
//                && Inventory.stream().id(Items.BLOOD_ESSENCE).isNotEmpty()
//                && !Inventory.isFull();
        return InventoryExtensions.doesNotContain(Items.BLOOD_ESSENCE_ACTIVE)
                && InventoryExtensions.contains(Items.BLOOD_ESSENCE)
                && InventoryExtensions.isNotFull();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item bloodEssence = Inventory.stream().id(Items.BLOOD_ESSENCE).first();

        if (bloodEssence.valid()) {
            logger.info("Attempting to activate blood essence");
//            boolean activated = bloodEssence.interact("Activate")
//                    && Condition.wait(() -> Inventory.stream().id(Items.BLOOD_ESSENCE_ACTIVE).isNotEmpty(), 75, 20);
            boolean activated = bloodEssence.interact("Activate")
                    && Condition.wait(() -> InventoryExtensions.contains(Items.BLOOD_ESSENCE_ACTIVE), 75, 20);
            logger.info(
                    activated
                        ? "Successfully activated blood essence"
                        : "Failed to activate blood essence"
            );
        }

    }

}
