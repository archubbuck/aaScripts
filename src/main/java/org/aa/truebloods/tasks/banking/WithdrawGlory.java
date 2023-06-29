package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.BankExtensions;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WithdrawGlory implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Bank.opened()
//                && !Inventory.isFull()
//                && Inventory.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isEmpty()
//                && Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isEmpty();
        return Bank.opened()
                && InventoryExtensions.isNotFull()
                && BankExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES)
                && InventoryExtensions.doesNotContain(Items.CHARGED_AMULET_OF_GLORIES)
                && EquipmentExtensions.doesNotContain(Items.CHARGED_AMULET_OF_GLORIES);
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item glory = Bank.stream().id(Items.CHARGED_AMULET_OF_GLORIES).first();

        logger.info("Attempting to withdraw an " + glory.name());

//        boolean gloryWithdrawn = glory.valid()
//                && Bank.withdraw(glory.id(), 1)
//                && Condition.wait(() -> Inventory.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isNotEmpty(), 75, 30);

        boolean gloryWithdrawn = glory.valid()
                && Bank.withdraw(glory.id(), 1)
                && Condition.wait(() -> InventoryExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES), 75, 30);

        logger.info(
                gloryWithdrawn
                        ? "Successfully withdrew " + glory.name()
                        : "Failed to withdraw " + glory.name()
        );

    }
}
