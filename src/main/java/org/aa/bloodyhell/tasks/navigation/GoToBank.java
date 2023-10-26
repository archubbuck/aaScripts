package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.constants.Areas;
import org.core.Banks;
import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.core.helpers.Conditions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

public class GoToBank extends Task {
    private final String amuletOfGlorySubStr = "Amulet of glory(";

    @Override
    public boolean activate() {
        return Conditions.all(
                Banks.EDGEVILLE.getTile().distance() > 5,
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
    public void execute(AbstractScript abstractScript) {
        if (!Banks.EDGEVILLE.getTile().loaded()) {
            Item item = Equipment.stream().nameContains(amuletOfGlorySubStr).first();
            boolean teleported = item.interact("Edgeville")
                    && Condition.wait(() -> Banks.EDGEVILLE.getTile().distance() <= 15, 10, 150);
        }

        boolean moved = Movement.step(Banks.EDGEVILLE.getTile().derive(Random.nextInt(-2,3), Random.nextInt(-2,3)))
                && Condition.wait(() -> Banks.EDGEVILLE.getTile().distance() <= 5, 10, 225);
    }

//    @Override
//    public boolean activate() {
//        return Conditions.all(
//                Bank.nearest().distance() > 5,
//                Conditions.any(
//                        EquipmentExtensions.doesNotContain(Items.RUNECRAFTING_CAPES),
//                        EquipmentExtensions.doesNotContain(Items.STAVES),
//                        EquipmentExtensions.doesNotContain(Items.AMULETS_OF_GLORY),
//                        InventoryExtensions.doesNotContain(Items.POUCHES),
//                        Conditions.all(
//                                InventoryExtensions.doesNotContain(Items.PURE_ESSENCE),
//                                !PouchTracker.INSTANCE.hasPouchToEmpty()
//                        )
//                )
//        );
//    }
//
//    @Override
//    public void execute(AbstractScript abstractScript) {
//        boolean navigated = Movement.builder(null).setToBank(true)
//                .setRunMin(45)
//                .setRunMax(75)
//                .setWalkUntil(() ->
//                        Objects.stream().name("Bank booth").nearest().first().distance() <= 5
//                ).move().getSuccess();
//    }
}
