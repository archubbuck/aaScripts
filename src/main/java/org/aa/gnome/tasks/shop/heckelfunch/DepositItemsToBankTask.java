package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class DepositItemsToBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final int COINS_ID = 995;

    @Override
    public boolean activate() {
        return Bank.opened()
                && Inventory.stream().anyMatch(item -> item.id() != COINS_ID);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to deposit all items (excluding coins)");

        boolean depositedItems = Bank.depositAllExcept(COINS_ID)
                && Condition.wait(() -> Inventory.stream().noneMatch(item -> item.id() != COINS_ID), 75, 30);

        logger.info(
                depositedItems
                        ? "We deposited our items!"
                        : "We failed to deposit our items in time!"
        );
    }
}
