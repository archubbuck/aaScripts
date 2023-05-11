package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;

public class CloseBankTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Bank.opened() && Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Closing the bank");
        if (!Bank.close() || !Condition.wait(() -> !Bank.opened(), 150, 15)) {
            abstractScript.setStatus("Unable to close the bank");
        }
    }

}
