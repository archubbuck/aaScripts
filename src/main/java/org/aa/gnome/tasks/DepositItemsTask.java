package org.aa.gnome.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.Arrays;
import java.util.logging.Logger;

public class DepositItemsTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final int[] itemIdsToKeep;

    public DepositItemsTask() {
        this.itemIdsToKeep = new int[]{};
    }

    public DepositItemsTask(int[] itemIdsToKeep) {
        this.itemIdsToKeep = itemIdsToKeep;
    }

    @Override
    public boolean activate() {
        return Bank.opened()
                && itemIdsToKeep.length < 1 || Inventory.stream().anyMatch(item -> Arrays.stream(itemIdsToKeep).noneMatch(itemIdToKeep -> itemIdToKeep == item.id()));
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to deposit items");

        boolean success =
                (itemIdsToKeep.length < 1
                        ? Bank.depositInventory()
                        : Bank.depositAllExcept(itemIdsToKeep))
                && Condition.wait(() -> {
                    return itemIdsToKeep.length < 1
                            ? Inventory.isEmpty()
                            : Inventory.stream().anyMatch(item -> Arrays.stream(itemIdsToKeep).anyMatch(itemIdToKeep -> itemIdToKeep != item.id()));
                }, 75, 30);

        logger.info(
                success
                        ? "We deposited the items!"
                        : "Failed to deposit the items in time!"
        );
    }
}
