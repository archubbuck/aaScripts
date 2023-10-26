package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.truebloods.helpers.PouchTracker;
import org.core.Task;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.Players;
import org.powbot.api.script.AbstractScript;

import java.util.List;
import java.util.logging.Logger;

public class EmptyPouch extends Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && Areas.BLOOD_ALTAR_AREA.contains(Players.local())
                && Inventory.stream().name("Colossal pouch").isNotEmpty()
                && PouchTracker.INSTANCE.hasPouchToEmpty();
//                && Inventory.stream().name("Pure essence").isEmpty()
//                && Inventory.stream().name("Colossal pouch").isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        List<Item> pouches = Inventory.stream().name(
                PouchTracker.INSTANCE.getPouchesToWithdraw()
        ).list();

        pouches.forEach(pouch -> {
            //  pouch.interact("Empty");
            boolean emptied = pouch.interact("Empty") && Condition.wait(() -> Inventory.stream().nameContains("Pure Essence").isNotEmpty(), 10, 150);
        });
    }

//    private boolean emptyPouch() {
//        return InventoryExtensions.contains(Items.PURE_ESSENCE) &&
//    }
}
