package org.aa.bloodyhell.tasks;

import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

import java.util.List;

public class WearEquipment extends Task {
    private final String[] equipmentNames = {
            "Dramen staff",
            "Hat of the eye",
            "Robe top of the eye",
            "Robe bottoms of the eye",
            "Boots of the eye",
            "Runecraft cape",
            "Amulet of Glory("
    };

    @Override
    public boolean activate() {
        return Inventory.stream().nameContains(equipmentNames).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (Bank.opened()) {
            boolean closed = Bank.close() && Condition.wait(() -> !Bank.opened(), 300, 10);
            System.out.println(closed ? "Closed the bank" : "Failed to close the bank");
            return;
        }

        for (Item item : Inventory.stream().nameContains(equipmentNames)) {
            String action = item.actions().contains("Wield") ? "Wield" : "Wear";
            boolean equipped = item.interact(action) && Condition.wait(() -> EquipmentExtensions.contains(item.id()), 300, 10);
        }
    }
}
