package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class WithdrawUnstrungBowTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && !Inventory.isFull()
                && Inventory.stream().id(FletchingScript.bow.getUnstrungId(), FletchingScript.bow.getStrungId()).isEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        int quantityToWithdraw = Math.min(14, Bank.stream().id(FletchingScript.bow.getUnstrungId()).first().stackSize());
        abstractScript.setStatus("Withdrawing " + quantityToWithdraw + " " + FletchingScript.bow.getUnstrungName());

        boolean withdrewUnstrungBows = Bank.withdraw(FletchingScript.bow.getUnstrungId(), quantityToWithdraw)
                && Condition.wait(() -> Inventory.stream().id(FletchingScript.bow.getUnstrungId()).isNotEmpty(), 75, 30);

        logger.info(
                (withdrewUnstrungBows ? "Withdrew " : "Failed to withdraw ")
                        + quantityToWithdraw + " " + FletchingScript.bow.getUnstrungName()
            );
    }
}
