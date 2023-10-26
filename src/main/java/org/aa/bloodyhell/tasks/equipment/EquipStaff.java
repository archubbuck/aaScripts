package org.aa.bloodyhell.tasks.equipment;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Game.Tab;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

public class EquipStaff extends Task {
    @Override
    public boolean activate() {
        return EquipmentExtensions.doesNotContain(Items.STAVES)
                && InventoryExtensions.contains(Items.STAVES);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (!Bank.opened() && !Game.tab(Tab.INVENTORY)) return;
        Item item = Inventory.stream().id(Items.STAVES).first();
        boolean equipped = item.interact("Wield") && Condition.wait(() -> EquipmentExtensions.contains(item.id()), 300, 10);
    }
}
