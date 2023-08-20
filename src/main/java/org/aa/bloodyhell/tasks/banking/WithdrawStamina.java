package org.aa.bloodyhell.tasks.banking;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.aa.truebloods.Utils;
import org.powbot.api.Notifications;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.script.ScriptManager;

public class WithdrawStamina implements Task {
    @Override
    public boolean activate() {
        return Utils.all(
                Bank.nearest().distance() <= 5,
                InventoryExtensions.doesNotContain(Items.STAMINA_POTIONS),
                Movement.energyLevel() <= 35
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.open()) return false;

        Item item = Bank.stream().id(Items.STAMINA_POTIONS).first();

        if (!item.valid()) {
            ScriptManager.INSTANCE.stop();
            Notifications.showNotification("You are missing stamina potions!");
            return false;
        }

        return Bank.withdraw(item, 1);
    }
}
