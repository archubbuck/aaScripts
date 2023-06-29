package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GroundItem;
import org.powbot.api.rt4.GroundItems;
import org.powbot.api.rt4.Inventory;

public class PickupTask extends AbstractTask {

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && GroundItems.stream().id(Constants.BRONZE_PICKAXE_ID).isNotEmpty()
                && Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        GroundItem groundItem = GroundItems.stream().id(Constants.BRONZE_PICKAXE_ID).viewable().nearest().first();

        long countOfGroundItem = GroundItems.stream().id(groundItem.id()).at(groundItem.tile()).count();

        abstractScript.setStatus("Picking up " + groundItem.name());

        if (!groundItem.interact("Take") || !Condition.wait(() -> GroundItems.stream().id(groundItem.id()).at(groundItem.tile()).count() < countOfGroundItem, 50, 150)) {
            abstractScript.setStatus("Unable to pickup " + groundItem.name());
        }
    }
}