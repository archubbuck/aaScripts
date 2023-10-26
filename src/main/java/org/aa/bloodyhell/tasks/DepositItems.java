package org.aa.bloodyhell.tasks;

import org.core.Banks;
import org.core.Task;
import org.jetbrains.annotations.Nullable;
import org.powbot.api.Condition;
import org.powbot.api.Filter;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.stream.item.InventoryItemStream;
import org.powbot.api.script.AbstractScript;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepositItems extends Task {
    private final String[] ignoredItemNames = {
            "Dramen staff",
            "Hat of the eye",
            "Robe top of the eye",
            "Robe bottoms of the eye",
            "Boots of the eye",
            "Runecraft cape",
            "Amulet of glory(",
            "Blood essence",
            "Colossal pouch",
            "Stamina potion",
            "Pure essence"
    };

    @Override
    public boolean activate() {
        return Banks.EDGEVILLE.getTile().distance() <= 25 && getItemsToDeposit().isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (!Bank.opened()) {
            boolean opened = Bank.open() && Condition.wait(Bank::opened, 10, 150);
            System.out.println(opened ? "Opened the bank" : "Failed to open the bank");
            if (!opened) {
                return;
            }
        }
//        String[] itemsToDeposit = getItemsToDeposit().list().stream().map(Item::name).toArray(String[]::new);
//        System.out.println(Arrays.toString(itemsToDeposit));
//        boolean deposited = Bank.depositAllExcept(itemsToDeposit);
        for (Item item : getItemsToDeposit()) {
            Bank.deposit(item.id(), Bank.Amount.ALL);
        }
//        boolean deposited = Bank.depositAllExcept(ignoredItemNames);
    }

    private InventoryItemStream getItemsToDeposit() {
        return Inventory.stream().filter(inventoryItem -> Arrays.stream(ignoredItemNames).noneMatch(ignoredItemName -> inventoryItem.name().contains(ignoredItemName)));
    }
}
