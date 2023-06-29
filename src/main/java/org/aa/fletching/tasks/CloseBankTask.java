package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class CloseBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && Inventory.stream().id(FletchingScript.bow.getUnstrungId()).isNotEmpty()
                && Inventory.stream().id(FletchingScript.BOW_STRING_ID).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Closing the bank");

        boolean closedTheBank = Bank.close()
                && Condition.wait(() -> !Bank.opened(), 75, 30);

        logger.info(closedTheBank
                ? "Closed the bank"
                : "Failed to close the bank"
        );
    }
}
