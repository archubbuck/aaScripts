package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

public class DepositToBankTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Bank.opened() && (Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isNotEmpty() || Inventory.isFull());
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Depositing inventory");
        if (!Bank.depositInventory() || !Condition.wait(() -> Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isEmpty() && !Inventory.isFull(), 150, 15)) {
            abstractScript.setStatus("Unable to deposit inventory");
        }
    }

}
