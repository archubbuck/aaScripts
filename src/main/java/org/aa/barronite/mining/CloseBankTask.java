package org.aa.barronite.mining;

import org.aa.barronite.Task;
import org.aa.barronite.BarroniteScript;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class CloseBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && Inventory.stream().id(BarroniteScript.BARRONITE_DEPOSIT_ID).isEmpty();
    }

    @Override
    public void execute() {
        logger.info("Closing the bank");

        boolean closedTheBank = Bank.close()
                && Condition.wait(() -> !Bank.opened(), 150, 15);

        logger.info(closedTheBank
                ? "Closed the bank"
                : "Failed to close the bank"
        );
    }
}
