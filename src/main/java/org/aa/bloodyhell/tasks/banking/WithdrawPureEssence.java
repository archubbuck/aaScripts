package org.aa.bloodyhell.tasks.banking;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Notifications;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.script.ScriptManager;

import static org.core.helpers.Conditions.all;
import static org.core.helpers.Conditions.any;

public class WithdrawPureEssence implements Task {
    @Override
    public boolean activate() {
        return all(
                Bank.nearest().distance() <= 5,
                any(
                        InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
                        InventoryExtensions.isNotFull()
                )
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.open()) return false;

        Item item = Bank.stream().id(Items.PURE_ESSENCE).first();

        if (!item.valid()) {
            ScriptManager.INSTANCE.stop();
            Notifications.showNotification("You are missing pure essence!");
            return false;
        }

        return Bank.withdraw(item.id(), Bank.Amount.ALL);
    }
}
