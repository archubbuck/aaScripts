package org.aa.bloodyhell.tasks;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.Game.Tab;
import org.powbot.api.script.AbstractScript;

public class DrinkStamina extends Task {

    @Override
    public boolean activate() {
        return InventoryExtensions.contains(Items.STAMINA_POTIONS)
                && Movement.energyLevel() <= 35;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (!Bank.opened() && !Game.tab(Tab.INVENTORY)) return;
        Item item = Inventory.stream().id(Items.STAMINA_POTIONS).first();
        boolean drank = item.interact("Drink") && Condition.wait(() -> !item.valid(), 50, 10);
    }
}
