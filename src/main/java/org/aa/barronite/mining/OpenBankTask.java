package org.aa.barronite.mining;

import org.aa.barronite.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;

import java.util.logging.Logger;

public class OpenBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    GameObject bankChest;

    @Override
    public boolean activate() {

        if (Bank.opened() || !Inventory.isFull()) {
            return false;
        }

        bankChest = Objects.stream().name("Bank chest").viewable().nearest().first();

//        return bankChest.valid();
        return bankChest.valid();
    }

    @Override
    public void execute() {
        logger.info("Opening the bank");

        //TODO: Handle situations where the bank chest isn't locatable

        bankChest.bounds(-32, 32, -64, 0, -32, 32);

        boolean openedTheBank = bankChest.interact("Use") && Condition.wait(Bank::opened, 150, 15);

        logger.info(openedTheBank
                ? "Opened the bank"
                : "Failed to open the bank"
        );
    }
}
