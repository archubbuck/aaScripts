package org.aa.barronite.mining;

import org.aa.barronite.BarroniteScript;
import org.aa.barronite.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class DepositItemsAtBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && Inventory.stream().id(BarroniteScript.BARRONITE_DEPOSIT_ID).isNotEmpty();
    }

    @Override
    public void execute() {
        logger.info("Depositing items");

        boolean depositedItems = Bank.depositAllExcept(BarroniteScript.BARRONITE_SHARDS_ID)
                && Condition.wait(() -> Inventory.stream().id(BarroniteScript.BARRONITE_DEPOSIT_ID).isEmpty(), 150, 15);

        logger.info(depositedItems ? "Deposited items" : "Failed to deposit items");
    }
}
