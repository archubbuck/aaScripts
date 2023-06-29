package org.aa.truebloods.tasks;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.List;
import java.util.logging.Logger;

public class EmptyColossalPouch implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Areas.BLOOD_ALTAR_AREA.contains(Players.local())
//                && (inventoryContainsEssence() || Varpbits.varpbit(261) != 0);
        return Areas.BLOOD_ALTAR_AREA.contains(Players.local())
                && InventoryExtensions.contains(Items.COLOSSAL_POUCH)
                && InventoryExtensions.doesNotContain(Items.PURE_ESSENCE)
                && PouchTracker.INSTANCE.hasPouchToEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (!Inventory.opened()) {
            Inventory.opened();
        }

        List<Item> pouches = Inventory.stream().name(
                PouchTracker.INSTANCE.getPouchesToWithdraw()
        ).list();

        pouches.forEach(pouch -> {
            if (pouch.interact("Empty")) {
                Condition.sleep(Random.nextInt(550, 650));
            }
        });

    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//
//        String craftAction = "Craft-rune";
//        GameObject altar = Objects.stream().name("Altar").action(craftAction).first();
//
//        if (!altar.valid()) {
//            logger.info("Altar invalid");
//            return;
//        }
//
//        if (!inventoryContainsEssence() && Varpbits.varpbit(261) != 0) {
//            logger.info("Attempting to empty pouch");
//
//            Item pouch = Inventory.stream().id(Items.COLOSSAL_POUCH).first();
//
//            int startingVarp = Varpbits.varpbit(261);
//            boolean emptied = pouch.interact("Empty") && Condition.wait(() -> startingVarp != Varpbits.varpbit(261), 75, 30);
//            logger.info(
//                    emptied ? "emptied" : "failed to empty"
//            );
//
//        }
//
//        boolean success = altar.interact(craftAction)
//                && Condition.wait(() -> !inventoryContainsEssence(), 75, 30);
//
//        logger.info(
//                "Crafting " + (success ? "successful" : "failed")
//        );
//    }
//
//    private boolean inventoryContainsEssence() {
//        return Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty();
//    }
}
