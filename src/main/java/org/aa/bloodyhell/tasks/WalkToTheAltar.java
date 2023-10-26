package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.tasks.navigation.*;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.rt4.Players;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class WalkToTheAltar extends Task {
    @Override
    public boolean activate() {
        return all(
                !Areas.BLOOD_ALTAR_AREA.contains(Players.local()),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (Areas.EDGEVILLE_AREA.contains(Players.local()) || Areas.FAIRY_RING_AREA.contains(Players.local())) {
            new GoToCave1().execute(abstractScript);
            return;
        }

        if (Areas.CAVE_1_AREA.contains(Players.local())) {
            new GoToCave2().execute(abstractScript);
            return;
        }

        if (Areas.CAVE_2_AREA.contains(Players.local())) {
            new GoToCave3().execute(abstractScript);
            return;
        }

        if (Areas.CAVE_3_AREA.contains(Players.local())) {
            new GoToCave4().execute(abstractScript);
            return;
        }

        if (Areas.CAVE_4_AREA.contains(Players.local())) {
            new GoToCave7().execute(abstractScript);
            return;
        }

        if (Areas.CAVE_7_AREA.contains(Players.local())) {
            new GoToBloodAltar().execute(abstractScript);
            return;
        }

        System.out.println("We are in an unexpected area!");
        System.out.println(Players.local().tile());
    }
}
