package org.aa.gnome.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;

import java.util.logging.Logger;

public class OpenBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return !Bank.opened()
                && Bank.inViewport();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to open the bank");

        boolean success = Bank.open()
                && Condition.wait(Bank::opened, 75, 30);

        logger.info(
                success
                        ? "We opened the bank!"
                        : "Failed to open the bank in time!"
        );
    }
}
