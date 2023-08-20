package org.aa.bloodyhell.tasks.banking;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.aa.truebloods.Utils;
import org.powbot.api.Notifications;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.script.ScriptManager;

public class WithdrawBloodEssenceActive implements Task {
    @Override
    public boolean activate() {
        return Utils.all(
                Bank.nearest().distance() <= 5,
                InventoryExtensions.doesNotContain(Items.BLOOD_ESSENCE_ACTIVE),
                InventoryExtensions.isNotFull()
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.open()) return false;

        Item item = Bank.stream().id(Items.BLOOD_ESSENCE_ACTIVE).first();

        if (!item.valid()) {
            ScriptManager.INSTANCE.stop();
            Notifications.showNotification("You are missing an activated blood essence!");
            return false;
        }

        return Bank.withdraw(item.id(), Bank.Amount.ALL);
    }
}
