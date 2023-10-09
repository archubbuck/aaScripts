package org.aa.bloodyhell.tasks.navigation;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.core.helpers.Conditions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import org.powbot.api.script.AbstractScript;

public class GoToBank implements Task {
    @Override
    public boolean activate() {
        return Conditions.all(
                Bank.nearest().distance() > 5,
                Conditions.any(
                        EquipmentExtensions.doesNotContain(Items.RUNECRAFTING_CAPES),
                        EquipmentExtensions.doesNotContain(Items.STAVES),
                        EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY),
                        InventoryExtensions.doesNotContain(Items.POUCHES),
                        Conditions.all(
                                InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
                                !PouchTracker.INSTANCE.hasPouchToEmpty()
                        )
                )
        );
    }

    @Override
//    public boolean execute(AbstractScript abstractScript) {
//        return Movement.moveToBank().getSuccess();
//    }
    public boolean execute(AbstractScript abstractScript) {
        return Movement.builder(null).setToBank(true)
                .setRunMin(45)
                .setRunMax(75)
                .setWalkUntil(() ->
                        Objects.stream().name("Bank booth").nearest().first().distance() <= 5
                ).move().getSuccess();
    }
}
