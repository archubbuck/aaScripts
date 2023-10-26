package org.aa.bloodyhell.tasks;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

import static org.core.helpers.Conditions.all;

public class FillPouch extends Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return all(
                Bank.nearest().distance() <= 5,
                InventoryExtensions.containsAll(Items.COLOSSAL_POUCH, Items.PURE_ESSENCE),
                PouchTracker.INSTANCE.hasPouchToFill()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Filling pouches");

        PouchTracker.INSTANCE.getSupportedPouches().forEach(p -> {
            if (p.getStatus()) {
                return;
            }
            Item pouch = Inventory.stream().name(p.getItemName()).first();
            if (!pouch.actions().contains("Fill")) {
                p.setStatus(true);
            } else {
//                fillPouch(pouch);
                boolean filled = pouch.interact("Fill") && Condition.wait(() -> !Inventory.isFull(), 10, 150);
            }
        });

//        Condition.sleep(Random.nextInt(600, 900));
    }

//    private boolean fillPouch(Item item) {
//        return InventoryExtensions.contains(Items.PURE_ESSENCE) && item.interact("Fill");
//    }
}
