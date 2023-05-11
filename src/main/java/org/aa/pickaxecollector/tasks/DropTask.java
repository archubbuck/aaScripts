package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.GroundItems;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.stream.item.InventoryItemStream;

public class DropTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).count() == 1 && !shouldPickup();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (!Inventory.opened()) {
            abstractScript.setStatus("Opening the inventory tab");
            if (!Inventory.open() || !Condition.wait(Inventory::opened, 150, 15)) {
                abstractScript.setStatus("Unable to open the inventory");
                return;
            }
        }

        InventoryItemStream inventoryItemStream = Inventory.stream().id(Constants.BRONZE_PICKAXE_ID);
        Item item = inventoryItemStream.first();

        long inventoryItemStreamCount = inventoryItemStream.count();

        abstractScript.setStatus("Dropping " + item.name());

        if (!Inventory.drop(item, Inventory.shiftDroppingEnabled()) || !Condition.wait(() -> Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).count() < inventoryItemStreamCount, 150, 15)) {
            abstractScript.setStatus("Unable to drop " + item.name());
        }

    }

    private boolean shouldPickup() {
        int MIN_COUNT_OF_ITEM_BEFORE_PICKUP = 6;
        int MAX_COUNT_OF_ITEM_BEFORE_PICKUP = 17;
        int countOfItemBeforePickup = Random.nextInt(MIN_COUNT_OF_ITEM_BEFORE_PICKUP, MAX_COUNT_OF_ITEM_BEFORE_PICKUP);
        return GroundItems.stream().id(Constants.BRONZE_PICKAXE_ID).count() >= countOfItemBeforePickup;
    }

}
