package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.EquipmentExtensions;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.aa.truebloods.Utils;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.AbstractScript;

public class GoToBank implements Task {
    @Override
    public boolean activate() {
        return Utils.all(
                Bank.nearest().distance() > 5,
                Utils.any(
                        EquipmentExtensions.doesNotContain(Items.RUNECRAFTING_CAPES),
                        EquipmentExtensions.doesNotContain(Items.STAVES),
                        EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY),
                        InventoryExtensions.doesNotContain(Items.POUCHES),
                        Utils.all(
                                InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
                                !PouchTracker.INSTANCE.hasPouchToEmpty()
                        )
                )
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        return Movement.moveToBank().getSuccess();
    }
}
