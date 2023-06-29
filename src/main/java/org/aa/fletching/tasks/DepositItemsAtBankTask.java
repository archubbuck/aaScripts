package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

import java.util.logging.Logger;

public class DepositItemsAtBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Bank.opened()
                && Inventory.stream().anyMatch(item -> item.id() != FletchingScript.bow.getUnstrungId() || item.id() != FletchingScript.BOW_STRING_ID);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Depositing items");

        boolean depositedItems = Bank.depositInventory()
                && Condition.wait(Inventory::isEmpty, 75, 30);

        logger.info(depositedItems ? "Deposited items" : "Failed to deposit items");
    }
}
