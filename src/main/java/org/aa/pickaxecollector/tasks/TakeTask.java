package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.pickaxecollector.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;

public class TakeTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isEmpty()
                && Objects.stream().name("Barrel").action("Take pickaxe").isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        GameObject barrel = Objects.stream().name("Barrel").action("Take pickaxe").nearest().first();

        barrel.bounds(-32, 32, -64, 0, -32, 32);

        if (!barrel.interact("Take pickaxe") || !Condition.wait(() -> Inventory.stream().id(Constants.BRONZE_PICKAXE_ID).isNotEmpty(), 150, 15)) {
            abstractScript.setStatus("Unable to click the barrel");
        }
    }
}
