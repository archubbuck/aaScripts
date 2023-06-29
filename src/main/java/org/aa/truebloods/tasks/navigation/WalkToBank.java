package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;
import org.powbot.dax.api.models.RunescapeBank;

import java.util.logging.Logger;

public class WalkToBank implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return !Bank.inViewport()
//                && (
//                    Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isEmpty()
//                            || (Inventory.stream().id(Items.PURE_ESSENCE).isEmpty() && Varpbits.varpbit(261) == 0)
//                );
//        if (Bank.inViewport()) {
//            return false;
//        }

//        return Utils.all(
//                !Bank.inViewport(),
//                !PouchTracker.INSTANCE.hasPouchToEmpty(),
//                Utils.any(
//                    EquipmentExtensions.doesNotContain(Items.CHARGED_AMULET_OF_GLORIES),
//                    InventoryExtensions.doesNotContain(Items.PURE_ESSENCE)
////                    Utils.all(
////                        InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
////                        Areas.BLOOD_ALTAR_AREA.contains(Players.local())
////                    )
//                )
//        );

        return Utils.all(
                !Bank.inViewport(),
                !ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to walk to the edgeville bank");

        boolean success = Movement.builder(RunescapeBank.EDGEVILLE.getPosition())
                .setToBank(true)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(true)
                .move()
                .getSuccess();

        logger.info(
                "Navigation " + (success ? "successful" : "failed")
        );
    }
}
