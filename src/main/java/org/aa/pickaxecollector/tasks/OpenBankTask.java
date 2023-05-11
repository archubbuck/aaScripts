package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

public class OpenBankTask extends AbstractTask {

    @Override
    public boolean activate() {

        if (Inventory.isFull()) {
            return true;
        }

        return Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).count() > 1
                && GroundItems.stream().id(Constants.BRONZE_PICKAXE_ID).isEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        GameObject bankChest = Objects.stream().name("Bank chest").nearest().first();

        if (!bankChest.inViewport()) {
            abstractScript.setStatus(bankChest.name() + " is not in the viewport");
            return;
        }

        bankChest.bounds(-32, 32, -64, 0, -32, 32);

        abstractScript.setStatus("Opening the bank");
        if (!bankChest.interact("Use") || !Condition.wait(Bank::opened, 150, 15)) {
            abstractScript.setStatus("Unable to open the bank");
            return;
        }

        abstractScript.setStatus("Depositing inventory");
        if (!Bank.depositInventory() || !Condition.wait(
                () -> Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isEmpty(), 150, 15)) {
            abstractScript.setStatus("Unable to deposit inventory");
        }
    }

}
