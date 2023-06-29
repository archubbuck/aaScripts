package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;

import java.util.logging.Logger;

public class OpenBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {

        if (Bank.opened()) {
            return false;
        }

        boolean missingUnstrungBow = Inventory.stream().id(FletchingScript.bow.getUnstrungId()).isEmpty();
        boolean missingBowString = Inventory.stream().id(FletchingScript.BOW_STRING_ID).isEmpty();

        return missingUnstrungBow || missingBowString;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Opening the bank");

        //TODO: Handle situations where the bank chest isn't locatable
        GameObject bankChest = Objects.stream().name("Bank chest").viewable().nearest().first();
        bankChest.bounds(-32, 32, -64, 0, -32, 32);

        boolean openedTheBank = bankChest.interact("Use") && Condition.wait(Bank::opened, 75, 30);

        logger.info(openedTheBank
                ? "Opened the bank"
                : "Failed to open the bank"
        );
    }
}
