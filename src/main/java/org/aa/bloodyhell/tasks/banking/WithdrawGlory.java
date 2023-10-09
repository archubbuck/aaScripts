package org.aa.bloodyhell.tasks.banking;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.core.helpers.Conditions;
import org.powbot.api.Notifications;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.script.ScriptManager;

public class WithdrawGlory implements Task {
    @Override
    public boolean activate() {
        return Conditions.all(
                Bank.nearest().distance() <= 5,
                InventoryExtensions.doesNotContain(Items.AMULETS_OF_GLORY),
                EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY)
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.open()) return false;

        Item item = Bank.stream().id(Items.AMULETS_OF_GLORY).first();

        if (!item.valid()) {
            ScriptManager.INSTANCE.stop();
            Notifications.showNotification("You are missing an amulet of glory!");
            return false;
        }

        return Bank.withdraw(item, 1);
    }
}
