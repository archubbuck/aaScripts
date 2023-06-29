package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Varpbits;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class OpenBank implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return !Bank.opened()
//                && Bank.inViewport()
//                && (
//                    Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isEmpty()
//                        || Inventory.stream().id(Items.PURE_ESSENCE).isEmpty()
//                        || pouchIsNotFull()
//                );

        return Utils.all(
                !Bank.opened(),
                Bank.inViewport(),
                !ConditionExtensions.readyToCraftBloodRunes()
//                Utils.any(
//                        EquipmentExtensions.doesNotContain(Items.CHARGED_AMULET_OF_GLORIES),
//                        InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
////                        !PouchTracker.INSTANCE.hasPouchToEmpty()
//                        PouchTracker.INSTANCE.hasPouchToFill()
//                )
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        logger.info("Attempting to open the bank");

        boolean bankOpened = Bank.open()
                && Condition.wait(Bank::opened, 75, 30);

        if (bankOpened) {
            PouchTracker.INSTANCE.varpbitChanged(Varpbits.varpbit(261));
        }

        logger.info(
                bankOpened
                        ? "Successfully opened the bank"
                        : "Failed to open the bank"
        );

    }

//    private boolean pouchIsNotFull() {
//        return Varpbits.varpbit(261) != 16;
//    }
}
