package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class FillColossalPouch implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return !State.pouchIsFull
//                && Inventory.stream().id(Items.COLOSSAL_POUCH).isNotEmpty()
//                && Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty();
//        return InventoryExtensions.contains(Items.COLOSSAL_POUCH)
//                && InventoryExtensions.contains(Items.PURE_ESSENCE)
//                && !State.pouchIsFull;
        logger.info("eval fill");
        return Utils.all(
                InventoryExtensions.containsAll(Items.COLOSSAL_POUCH, Items.PURE_ESSENCE),
                PouchTracker.INSTANCE.hasPouchToFill()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Emptying pouches");

        PouchTracker.INSTANCE.getSupportedPouches().forEach(p -> {
            if (p.getStatus()) {
                return;
            }
            Item pouch = Inventory.stream().name(p.getItemName()).first();
            if (!pouch.actions().contains("Fill")) {
                p.setStatus(true);
            } else {
                fillPouch(pouch);
            }
        });

        Condition.sleep(Random.nextInt(600, 900));
    }

    private boolean fillPouch(Item item) {
        return InventoryExtensions.contains(Items.PURE_ESSENCE) && item.interact("Fill");
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//
//        Item colossalPouch = Inventory.stream().id(Items.COLOSSAL_POUCH).first();
//
////        if (colossalPouch.actions().stream().anyMatch(s -> s.equals("Empty"))) {
////            logger.info("updated");
////            State.pouchIsFull = true;
////            return;
////        }
//
////        logger.info("breach");
//
//        logger.info("Attempting to fill the colossal pouch");
//
////        long initialCountOfPureEssence = getCountOfPureEssence();
//        long initialCountOfPureEssence = InventoryExtensions.count(Items.PURE_ESSENCE);
//
////        logger.info(""+Inventory.stream().id(Items.COLOSSAL_POUCH).count());
//
//        boolean filledColossalPouch = colossalPouch.interact("Fill")
//                && Condition.wait(() -> State.pouchIsFull() || initialCountOfPureEssence < InventoryExtensions.count(Items.PURE_ESSENCE), 75, 10);
//
////       if (initialCountOfPureEssence < InventoryExtensions.count(Items.PURE_ESSENCE)) {
////           State.essenceInPouch =
////       }
//
////        logger.info(
////                filledColossalPouch || State.pouchIsFull
////                        ? "Successfully filled the colossal pouch"
////                        : "Failed to fill the colossal pouch"
////        );
//
//    }
//
//    private long getCountOfPureEssence() {
//        return Inventory.stream().id(Items.PURE_ESSENCE).count();
//    }

}
