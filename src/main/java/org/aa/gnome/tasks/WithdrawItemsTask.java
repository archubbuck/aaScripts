package org.aa.gnome.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class WithdrawItemsTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final String itemName;
    private final int itemId;
    private final int itemQuantity;

    public WithdrawItemsTask(String itemName, Integer itemId, Integer itemQuantity) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
    }

    @Override
    public boolean activate() {
        return Bank.opened() && !hasItems();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        String quantityOfItemName = itemQuantity + " of " + itemName;

        logger.info("Withdrawing " + quantityOfItemName);

        boolean success = Bank.withdraw(itemId, itemQuantity)
                && Condition.wait(this::hasItems, 75, 30);

        logger.info(
                success
                        ? "Successfully withdrew " + quantityOfItemName
                        : "Failed to withdraw " + quantityOfItemName
        );
    }

    private boolean hasItems() {
        return Inventory.stream().id(itemId).count(true) >= itemQuantity;
    }
}
