package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.script.AbstractScript;

import java.util.Arrays;
import java.util.logging.Logger;

public class DepositItems implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Bank.opened()
//                && inventoryContainsBankableItems();
        return Bank.opened()
                && !InventoryExtensions.containsOnly(Items.LOCKED_INVENTORY_ITEMS);
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        logger.info("Attempting to deposit items");

//        boolean depositedItems = Bank.depositAllExcept(Items.LOCKED_INVENTORY_ITEMS)
//                && Condition.wait(() -> !inventoryContainsBankableItems(), 75, 30);

        boolean depositedItems = Bank.depositAllExcept(Items.LOCKED_INVENTORY_ITEMS)
                && Condition.wait(() -> InventoryExtensions.containsOnly(Items.LOCKED_INVENTORY_ITEMS), 75, 30);

        logger.info(
                depositedItems
                        ? "Successfully deposited items"
                        : "Failed to deposit items"
        );

    }

    private boolean inventoryContainsBankableItems() {
        return Inventory.stream().filter(item -> Arrays.stream(Items.LOCKED_INVENTORY_ITEMS).noneMatch(itemId -> itemId == item.id())).isNotEmpty();
    }
}
