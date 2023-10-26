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

public class EquipGlory extends Task {

    @Override
    public boolean activate() {
        return EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY)
                && InventoryExtensions.contains(Items.AMULETS_OF_GLORY);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (!Bank.opened() && !Game.tab(Tab.INVENTORY)) return;
        Item item = Inventory.stream().id(Items.AMULETS_OF_GLORY).first();
        boolean equipped = item.interact("Wear") && Condition.wait(() -> EquipmentExtensions.contains(item.id()), 300, 10);
    }
}
