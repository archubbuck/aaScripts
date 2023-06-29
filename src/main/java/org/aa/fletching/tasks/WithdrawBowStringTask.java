package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class WithdrawBowStringTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && !Inventory.isFull()
                && Inventory.stream().id(FletchingScript.BOW_STRING_ID, FletchingScript.bow.getStrungId()).isEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        int quantityToWithdraw = Math.min(14, Bank.stream().id(FletchingScript.BOW_STRING_ID).first().stackSize());
        abstractScript.setStatus("Withdrawing " + quantityToWithdraw + " Bow String");

        boolean withdrewBowString = Bank.withdraw(FletchingScript.BOW_STRING_ID, quantityToWithdraw)
                && Condition.wait(() -> Inventory.stream().id(FletchingScript.BOW_STRING_ID).isNotEmpty(), 75, 30);

        logger.info(
                (withdrewBowString ? "Withdrew " : "Failed to withdraw ") + quantityToWithdraw + " Bow String"
        );
    }
}
