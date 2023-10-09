package org.aa.bloodyhell.tasks;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.Game.Tab;
import org.powbot.api.script.AbstractScript;

public class DrinkStamina implements Task {
    @Override
    public boolean activate() {
        return InventoryExtensions.contains(Items.STAMINA_POTIONS)
                && Movement.energyLevel() <= 35;
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.opened() && !Game.tab(Tab.INVENTORY)) return false;
        Item item = Inventory.stream().id(Items.STAMINA_POTIONS).first();
        return item.interact("Drink") && Condition.wait(() -> !item.valid(), 50, 10);
    }
}
