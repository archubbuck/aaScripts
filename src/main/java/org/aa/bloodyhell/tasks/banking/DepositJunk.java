package org.aa.bloodyhell.tasks.banking;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.core.helpers.Conditions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.script.AbstractScript;

public class DepositJunk implements Task {
    @Override
    public boolean activate() {
        return Conditions.all(
                Bank.nearest().distance() <= 5,
                !InventoryExtensions.containsOnly(Items.PROTECTED_ITEMS)
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        if (!Bank.open()) return false;
        return Bank.depositAllExcept(Items.PROTECTED_ITEMS)
                && Condition.wait(() -> InventoryExtensions.containsOnly(Items.PROTECTED_ITEMS), 75, 30);
    }
}
