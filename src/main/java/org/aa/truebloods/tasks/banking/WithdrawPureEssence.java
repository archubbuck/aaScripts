package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.BankExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WithdrawPureEssence implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Bank.opened()
//                && !Inventory.isFull()
//                && Bank.stream().id(Items.PURE_ESSENCE).isNotEmpty();
        return Bank.opened()
                && InventoryExtensions.isNotFull()
                && BankExtensions.contains(Items.PURE_ESSENCE);
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item pureEssence = Bank.stream().id(Items.PURE_ESSENCE).first();

        logger.info("Attempting to withdraw pure essence");

//        boolean pureEssenceWithdrawn = pureEssence.valid()
//                && Bank.withdraw(pureEssence.id(), Bank.Amount.ALL)
//                && Condition.wait(() -> Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty(), 75, 30);

        boolean pureEssenceWithdrawn = pureEssence.valid()
                && Bank.withdraw(pureEssence.id(), Bank.Amount.ALL)
                && Condition.wait(() -> Inventory.isFull() && InventoryExtensions.contains(Items.PURE_ESSENCE), 75, 30);

        logger.info(
                pureEssenceWithdrawn
                        ? "Successfully withdrew pure essence"
                        : "Failed to withdraw pure essence"
        );

    }
}
