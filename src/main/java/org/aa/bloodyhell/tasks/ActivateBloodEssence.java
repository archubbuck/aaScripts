package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Items;
import org.core.Task;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Game.Tab;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

public class ActivateBloodEssence extends Task {
    @Override
    public boolean activate() {
        return InventoryExtensions.doesNotContain(Items.BLOOD_ESSENCE_ACTIVE)
                && InventoryExtensions.contains(Items.BLOOD_ESSENCE)
                && InventoryExtensions.isNotFull();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (Bank.opened()) {
            boolean closed = Bank.close() && Condition.wait(() -> !Bank.opened(), 300, 10);
            if (!closed) return;
        }
        if (!Game.tab(Tab.INVENTORY)) return;
        Item item = Inventory.stream().id(Items.BLOOD_ESSENCE).first();
        boolean activated = item.interact("Activate") && Condition.wait(() -> InventoryExtensions.contains(Items.BLOOD_ESSENCE_ACTIVE), 300, 10);
    }
}
