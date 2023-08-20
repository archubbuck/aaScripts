package org.aa.bloodyhell.tasks.equipment;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.EquipmentExtensions;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Game.Tab;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

public class EquipGlory implements Task {
    @Override
    public boolean activate() {
        return EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY)
                && InventoryExtensions.contains(Items.AMULETS_OF_GLORY);
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.opened() && !Game.tab(Tab.INVENTORY)) return false;
        Item item = Inventory.stream().id(Items.AMULETS_OF_GLORY).first();
        return item.interact("Wear") && Condition.wait(() -> EquipmentExtensions.contains(item.id()), 300, 10);
    }
}
